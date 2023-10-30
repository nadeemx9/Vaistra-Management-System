package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import com.vaistra.repositories.DemoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class GetRecordConfig {

    @Autowired
    private DemoRepository demoRepository;

    @Bean
    @Qualifier("demoGetDataJob")
    public Job demoGetDataJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("countryReadJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepGetData(jobRepository,transactionManager))
                .build();
    }

    @Bean
    public Step chunkStepGetData(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("countryReaderStep",jobRepository)
                .<DemoCSV, DemoCSV>chunk(10000,transactionManager)
                .reader(demoCSVFlatFileItemDataReader())
                .writer(demoCSVItemWriterData())
                .allowStartIfComplete(true)
                .taskExecutor(taskExecutorData())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutorData(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10); // Set the number of concurrent threads
        taskExecutor.setMaxPoolSize(20); // Set the maximum number of threads
        taskExecutor.setQueueCapacity(30); // Set the queue capacity for pending tasks
        return taskExecutor;
    }

    @Bean
    @StepScope
    public ItemWriter<DemoCSV> demoCSVItemWriterData(){
        List<DemoCSV> collectedData = new ArrayList<>();

        return items -> {
            // Collect the items in a list
            collectedData.addAll((Collection<? extends DemoCSV>) items);
        };
    }

//    @Bean
//    @StepScope
//    public ItemProcessor<DemoCSV, DemoCSV> demoCSVItemProcessorData(){
//        // Create and configure your item processor if needed
//        return item -> {
//            // Process the item (e.g., apply business logic)
//            return item;
//        };
//    }

    @Bean
    @StepScope
    public ItemReader<DemoCSV> demoCSVFlatFileItemDataReader(){
//        Sort sort = Sort.by("asc");
//
//        Pageable pageable = PageRequest.of(1000, 0, sort);
//
//        Page<DemoCSV> demoCSV = demoRepository.findAll(pageable);
//
//        System.out.println(demoCSV.iterator().next().getDate() + ":" + demoCSV.iterator().next().getTime());

        LocalDate date1 = LocalDate.of(2000,1,1);
        LocalDate date2 = LocalDate.of(2007,10,18);

//        List<DemoCSV> demoCSV = demoRepository.findMinuteTimestamps(date1,date2);

//        List<DemoCSV> d1 = (List<DemoCSV>) demoCSV;

//        return new ListItemReader<>(demoCSV);
        return null;
    }

}
