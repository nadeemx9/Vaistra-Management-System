package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.config.spring_batch.CountryBatch.CountryProcessor;
import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.entities.DemoCSV;
import com.vaistra.entities.cscv.Country;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

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
                .<DemoCSV, DemoCSV>chunk(1000000000,transactionManager)
                .reader(demoCSVFlatFileItemReader)
                .processor(demoCSVItemProcessorprocessor())
                .writer(demoCSVItemWriterwriter())
                .allowStartIfComplete(true)
                .build();
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
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(DemoCSV.class);
                }})
                .linesToSkip(1)
                .strict(false)  // Set to non-strict mode
                .build();
    }
}