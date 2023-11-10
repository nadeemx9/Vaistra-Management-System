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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    public Job stateReaderJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, FlatFileItemReader<State> reader){
        return new JobBuilder("stateReaderJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepState(jobRepository,platformTransactionManager,reader))
                .build();
    }

    @Bean
    public Step chunkStepState(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<State> reader){
        return new StepBuilder("stateReaderStep",jobRepository)
                .<State,State>chunk(50000,transactionManager)
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
        taskExecutor.setCorePoolSize(10); // Set the number of concurrent threads
        taskExecutor.setMaxPoolSize(20); // Set the maximum number of threads
        taskExecutor.setQueueCapacity(30); // Set the queue capacity for pending tasks
        return taskExecutor;
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
                .fieldSetMapper(    new BeanWrapperFieldSetMapper<>() {
                    @Override
                    public State mapFieldSet(FieldSet fs)  {
                        String stateName = fs.readString("stateName");
                        String countryName = fs.readString("countryName");
                        boolean active = fs.readString("status").equalsIgnoreCase("true");
                        State state = new State();
                        synchronized (this) {
                            state.setCountry(getCountry(countryName));
                        }
                        state.setStateName(stateName);
                        state.setStatus(active);
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
//    private final ConcurrentHashMap<String, Country> countries = new ConcurrentHashMap<>();

    private final Map<String, Lock> countryLocks = new ConcurrentHashMap<>();

    private Country getCountry(String countryName){
        // Use a dedicated lock object for each countryName
        countryLocks.putIfAbsent(countryName, new ReentrantLock());
        Lock countryLock = countryLocks.get(countryName);
        countryLock.lock();
        try {
            Country country = countryRepository.findByCountryNameIgnoreCase(countryName);
            if (country == null) {
                // Double-check inside the synchronized block
                synchronized (this) {
                    country = new Country();
                    country.setCountryName(countryName);
                    country.setStatus(true);
                    countryRepository.saveAndFlush(country);
                }
            }
            System.out.print(country.getCountryName() + "\t");
            return country;
        } finally {
            countryLock.unlock();
        }
    }
}