package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.config.spring_batch.CountryBatch.CountryProcessor;
import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.entities.DemoCSV;
import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class DemoBatchConfig {

    @Bean
    @Qualifier("demoReaderJob")
    public Job demoReaderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<DemoCSV> reader){
        return new JobBuilder("countryReadJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepdemo(jobRepository,transactionManager,reader))
                .build();
    }

    @Bean
    public Step chunkStepdemo(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<DemoCSV> demoCSVFlatFileItemReader){
        return new StepBuilder("countryReaderStep",jobRepository)
                .<DemoCSV, DemoCSV>chunk(10000,transactionManager)
                .reader(demoCSVFlatFileItemReader)
                .processor(demoCSVItemProcessorprocessor())
                .writer(demoCSVItemWriterwriter())
                .allowStartIfComplete(true)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10); // Set the number of concurrent threads
        taskExecutor.setMaxPoolSize(20); // Set the maximum number of threads
        taskExecutor.setQueueCapacity(30); // Set the queue capacity for pending tasks
        return taskExecutor;
    }

    @Bean
    @StepScope
    public ItemWriter<DemoCSV> demoCSVItemWriterwriter(){
        return new DemoWriter();
    }

    @Bean
    @StepScope
    public ItemProcessor<DemoCSV, DemoCSV> demoCSVItemProcessorprocessor(){
        return new CompositeItemProcessor<>(new DemoProcessor());
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DemoCSV> demoCSVFlatFileItemReader(@Value("#{jobParameters[inputFileDemo]}") String pathToFile){
        return new FlatFileItemReaderBuilder<DemoCSV>()
                .name("countryReader")
                .resource(new FileSystemResource(new File(pathToFile)))
                .delimited()
                .names(new String[]{
                        "date","time"
                })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<DemoCSV>() {
                    public DemoCSV mapFieldSet(FieldSet fs){
                        String dt = fs.readString("date");
                        String tm = fs.readString("time");
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                        LocalDate date = LocalDate.parse(dt,dateFormatter);
                        LocalTime time = LocalTime.parse(tm,timeFormatter);

                        DemoCSV csv = new DemoCSV();

                        csv.setDate(date);
                        csv.setTime(time);

                        return csv;
                    }
                    {
                        setTargetType(DemoCSV.class);
                    }
                })
                .linesToSkip(1)
                .strict(false)  // Set to non-strict mode
                .build();
    }
}