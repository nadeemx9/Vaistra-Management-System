package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.dto.cscv.StateDto;
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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

@Configuration
public class StateBatchConfig {
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public StateBatchConfig(StateRepository stateRepository,  CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Bean
    @Qualifier("stateReaderJob")
    public Job stateReaderJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, FlatFileItemReader<StateDto> reader){
        return new JobBuilder("stateReaderJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepState(jobRepository,platformTransactionManager,reader))
                .build();
    }

    @Bean
    public Step chunkStepState(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<StateDto> reader){
        return new StepBuilder("stateReaderStep",jobRepository)
                .<StateDto,State>chunk(10000,transactionManager)
                .reader(reader)
                .processor(stateProcessor())
                .writer(stateWriter())
                .allowStartIfComplete(true)
                .taskExecutor(stateTaskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor stateTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(15);
        taskExecutor.setMaxPoolSize(25);
        taskExecutor.setQueueCapacity(50);
        return taskExecutor;
    }

    @Bean
    @StepScope
    public ItemWriter<State> stateWriter(){
        return new StateWriter();
    }

    @Bean
    @StepScope
    public ItemProcessor<StateDto,State> stateProcessor(){
        return new CompositeItemProcessor<>(new StateProcessor(countryRepository));
    }

    @Bean
    @StepScope
    public FlatFileItemReader<StateDto> stateReader(@Value("#{jobParameters[inputFileState]}") String pathToFile){
        return new FlatFileItemReaderBuilder<StateDto>()
                .name("stateReader")
                .resource(new FileSystemResource(new File(pathToFile)))
                .delimited()
                .names(new String[]{
                        "countryName","stateName","status"
                })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(StateDto.class);
                }})
                .linesToSkip(1)
                .strict(false)  // Set to non-strict mode
                .build();
    }
//    private final ConcurrentHashMap<String, Country> countries = new ConcurrentHashMap<>();

//    private final Map<String, Lock> countryLocks = new ConcurrentHashMap<>();
//
//    private Country getCountry(String countryName){
//        // Use a dedicated lock object for each countryName
//        countryLocks.putIfAbsent(countryName, new ReentrantLock());
//        Lock countryLock = countryLocks.get(countryName);
//        countryLock.lock();
//        try {
//            Country country = countryRepository.findByCountryNameIgnoreCase(countryName);
//            if (country == null) {
//                // Double-check inside the synchronized block
//                synchronized (this) {
//                    country = new Country();
//                    country.setCountryName(countryName);
//                    country.setStatus(true);
//                    countryRepository.saveAndFlush(country);
//                }
//            }
//            System.out.print(country.getCountryName() + "\t");
//            return country;
//        } finally {
//            countryLock.unlock();
//        }
//    }
}