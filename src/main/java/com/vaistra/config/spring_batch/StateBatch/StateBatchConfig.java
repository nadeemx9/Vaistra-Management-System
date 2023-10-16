package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.dto.cscv.StateDto;
import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.StateRepository;
import com.vaistra.utils.AppUtils;
import lombok.Getter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import java.io.File;

@Configuration
public class StateBatchConfig {
    @Getter
    private static long counter;
    private final StateRepository stateRepository;
    private final AppUtils appUtils;
    private final CountryRepository countryRepository;

    @Autowired
    public StateBatchConfig(StateRepository stateRepository, AppUtils appUtils, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.appUtils = appUtils;
        this.countryRepository = countryRepository;
    }

    @Bean
    @Qualifier("stateReaderJob")
    public Job stateReaderJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, FlatFileItemReader<State> reader){
        return new JobBuilder("stateReaderJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepState(jobRepository,platformTransactionManager,reader))
                .build();
    }

    @Bean
    public Step chunkStepState(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<State> reader){
        return new StepBuilder("stateReaderStep",jobRepository)
                .<State,State>chunk(500000,transactionManager)
                .reader(reader)
                .processor(stateProcessor())
                .writer(stateWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<State> stateWriter(){
        System.out.println("In Writer Method");
        return new StateWriter();
    }

    @Bean
    @StepScope
    public ItemProcessor<State,State> stateProcessor(){
        System.out.println("In Processor Method");
        return new CompositeItemProcessor<>(new StateProcessor());
    }

    @Bean
    @StepScope
    public FlatFileItemReader<State> stateReader(@Value("#{jobParameters[inputFileState]}") String pathToFile){
        System.out.println("In Reader Method");
        return new FlatFileItemReaderBuilder<State>()
                .name("stateReader")
                .resource(new FileSystemResource(new File(pathToFile)))
                .delimited()
                .names(new String[]{
                        "countryName","stateName","status"
                })
                .fieldSetMapper(    new BeanWrapperFieldSetMapper<State>() {
                    @Override
                    public State mapFieldSet(FieldSet fs) throws BindException {
                        String stateName = fs.readString("stateName");
                        String countryName = fs.readString("countryName");
                        boolean active = fs.readString("status").equalsIgnoreCase("true");

                        Country country = countryRepository.findByCountryNameIgnoreCase(countryName);

                            State state = new State();
                            state.setStateName(stateName);
                            state.setStatus(active);

                            if (country == null) {
                                country = new Country();
                                country.setCountryName(countryName);
                                country.setStatus(true);
                                countryRepository.save(country);
                            }
                            state.setCountry(country);

                        return state;
                    }

                    @Override
                    public void setTargetType(Class<? extends State> type) {
                        super.setTargetType(type);
                    }
                })
                .linesToSkip(1)
                .strict(false)  // Set to non-strict mode
                .build();
    }
}