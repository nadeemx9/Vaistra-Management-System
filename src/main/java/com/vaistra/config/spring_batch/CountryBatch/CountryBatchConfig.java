package com.vaistra.config.spring_batch.CountryBatch;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CountryBatchConfig {
//    @Bean
//    @Qualifier("countryReaderJob")
//    public Job countryReaderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Country> reader){
//        return new JobBuilder("countryReadJob",jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(chunkStepCountry(jobRepository,transactionManager,reader))
//                .build();
//    }
//
//    @Bean
//    public Step chunkStepCountry(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Country> reader){
//        return new StepBuilder("countryReaderStep",jobRepository)
//                 .<Country, Country>chunk(500000,transactionManager)
//                .reader(reader)
//                .processor(processor())
//                .writer(writer())
//                .allowStartIfComplete(true)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<Country> writer(){
//        return new CountryWriter();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<Country, Country> processor(){
//        return new CompositeItemProcessor<>(new CountryProcessor());
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<Country> reader(@Value("#{jobParameters[inputFile]}") String pathToFile){
//        return new FlatFileItemReaderBuilder<Country>()
//                .name("countryReader")
//                .resource(new FileSystemResource(new File(pathToFile)))
//                .delimited()
//                .names(new String[]{
//                        "countryName","Status"
//                })
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                    setTargetType(Country.class);
//                }})
//                .linesToSkip(1)
//                .strict(false)  // Set to non-strict mode
//                .build();
//    }
}