package com.vaistra.config.spring_batch.StateBatch;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StateBatchConfig {
//    private final StateRepository stateRepository;
//    private final CountryRepository countryRepository;
//
//    @Autowired
//    public StateBatchConfig(StateRepository stateRepository, CountryRepository countryRepository) {
//        this.stateRepository = stateRepository;
//        this.countryRepository = countryRepository;
//    }
//
//    @Bean
//    @Qualifier("stateReaderJob")
//    public Job stateReaderJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, FlatFileItemReader<State> reader){
//        return new JobBuilder("stateReaderJob",jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(chunkStepState(jobRepository,platformTransactionManager,reader))
//                .build();
//    }
//
//    @Bean
//    public Step chunkStepState(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<State> reader){
//        return new StepBuilder("stateReaderStep",jobRepository)
//                .<State,State>chunk(500000,transactionManager)
//                .reader(reader)
//                .processor(stateProcessor())
//                .writer(stateWriter())
//                .allowStartIfComplete(true)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<State> stateWriter(){
//        System.out.println("In Writer Method");
//        return new StateWriter();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<State,State> stateProcessor(){
//        System.out.println("In Processor Method");
//        return new CompositeItemProcessor<>(new StateProcessor());
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<State> stateReader(@Value("#{jobParameters[inputFileState]}") String pathToFile){
//        System.out.println("In Reader Method");
//        return new FlatFileItemReaderBuilder<State>()
//                .name("stateReader")
//                .resource(new FileSystemResource(new File(pathToFile)))
//                .delimited()
//                .names(new String[]{
//                        "countryName","stateName","isActive"
//                })
//                .fieldSetMapper(    new BeanWrapperFieldSetMapper<>() {
//                    @Override
//                    public State mapFieldSet(FieldSet fs) throws BindException {
//                        State state = new State();
//                        String name = fs.readString("countryName");
//                        String sname = fs.readString("stateName");
//                        boolean active = fs.readString("isActive").equalsIgnoreCase("true");
//
//                        Country country = countryRepository.findByCountryNameIgnoreCase(name);
//
//                        if(country == null){
//                            country = new Country();
//                            country.setCountryName(name);
//                            country.setStatus(true);
//                            countryRepository.save(country);
//                        }
//
//                        if(!stateRepository.existsByStateNameIgnoreCase(sname)) {
//                            state.setCountry(country);
//                            state.setStateName(sname);
//                            state.setStatus(active);
//                            return state;
//                        }
//                        return null;
//                    }
//                    @Override
//                    public void setTargetType(Class<? extends State> type) {
//                        super.setTargetType(type);
//                    }
//
//                })
//                .linesToSkip(1)
//                .strict(false)  // Set to non-strict mode
//                .build();
//    }
}