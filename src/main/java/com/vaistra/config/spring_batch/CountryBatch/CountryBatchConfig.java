package com.vaistra.config.spring_batch.CountryBatch;

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
public class CountryBatchConfig {
    @Bean
    @Qualifier("countryReaderJob")
    public Job countryReaderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Country> reader){
        return new JobBuilder("countryReadJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStepCountry(jobRepository,transactionManager,reader))
                .build();
    }

    @Bean
    public Step chunkStepCountry(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Country> reader){
        return new StepBuilder("countryReaderStep",jobRepository)
                 .<Country, Country>chunk(500000,transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Country> writer(){
        return new CountryWriter();
    }

    @Bean
    @StepScope
    public ItemProcessor<Country, Country> processor(){
        return new CompositeItemProcessor<>(new CountryProcessor());
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Country> reader(@Value("#{jobParameters[inputFile]}") String pathToFile){
        return new FlatFileItemReaderBuilder<Country>()
                .name("countryReader")
                .resource(new FileSystemResource(new File(pathToFile)))
                .delimited()
                .names(new String[]{
                        "countryName","status"
                })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Country.class);
                }})
                .linesToSkip(1)
                .strict(false)  // Set to non-strict mode
                .build();
    }
}