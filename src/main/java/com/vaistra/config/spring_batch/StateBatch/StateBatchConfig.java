package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.StateRepository;
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
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public StateBatchConfig(StateRepository stateRepository, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
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
                        "countryName","stateName","isActive"
                })
                .fieldSetMapper(    new BeanWrapperFieldSetMapper<>() {
                    @Override
                    public State mapFieldSet(FieldSet fs) throws BindException {
                        State state = new State();
                        String name = fs.readString("countryName");
                        String sname = fs.readString("stateName");
                        boolean active = fs.readString("isActive").equalsIgnoreCase("true");

                        Country country = countryRepository.findByCountryNameIgnoreCase(name);

                        if(country == null){
                            country = new Country();
                            country.setCountryName(name);
                            country.setStatus(true);
                            countryRepository.save(country);
                        }

                        if(!stateRepository.existsByStateNameIgnoreCase(sname)) {
                            state.setCountry(country);
                            state.setStateName(sname);
                            state.setStatus(active);
                            return state;
                        }
                        return null;
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