package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
@EnableBatchProcessing
public class GetRecordConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    @Qualifier("exportDemoJob")
    public Job exportDemoJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemWriter<DemoCSV> exportItemWriter, ItemReader<DemoCSV> exportReader){
        return new JobBuilder("exportDemoJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exportChunkStepdemo(jobRepository,transactionManager,exportItemWriter,exportReader))
                .build();
    }

    @Bean
    public Step exportChunkStepdemo(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemWriter<DemoCSV> exportItemWriter, ItemReader<DemoCSV> exportReader){
        return new StepBuilder("countryReaderStep",jobRepository)
                .<DemoCSV, DemoCSV>chunk(10000,transactionManager)
                .writer(exportItemWriter)
                .reader(exportReader)
                .allowStartIfComplete(true)
                .taskExecutor(exportTaskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor exportTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(15); // Set the number of concurrent threads
        taskExecutor.setMaxPoolSize(25); // Set the maximum number of threads
        taskExecutor.setQueueCapacity(50); // Set the queue capacity for pending tasks
        return taskExecutor;
    }

    @Bean
    @StepScope
    public ItemReader<DemoCSV> exportReader(@Value("#{jobParameters[date1]}") String date1,@Value("#{jobParameters[date2]}") String date2){
        int pageSize = 403200;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        List<Iterator<DemoCSV>> demoCSVIterators = new ArrayList<>();

        int page = 0;

        LocalDate localDate = startDate;

        while (localDate.isBefore(endDate)) {
            PageRequest pageRequest = PageRequest.of(page, pageSize);

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
            Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.between(root.get("date"), startDate, endDate));

            Path<LocalDate> datePath = root.get("date");
            Path<LocalTime> timePath = root.get("time");
            Order dateOrder = criteriaBuilder.asc(datePath);
            Order timeOrder = criteriaBuilder.asc(timePath);
            criteriaQuery.orderBy(dateOrder, timeOrder);

            TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);
//            query.setFirstResult(0);
            query.setMaxResults(pageRequest.getPageSize());

            Iterator<DemoCSV> iterator = query.getResultList().iterator();
            demoCSVIterators.add(page,iterator);
            localDate = localDate.plusDays(280); // Adjust based on your requirement
            System.out.println(localDate);
            page++;
        }
//        for(LocalDate date = localDate; date.isBefore(endDate);){
//            PageRequest pageRequest = PageRequest.of(page, pageSize);
//
//            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//            CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
//            Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);
//            criteriaQuery.select(root)
//                    .where(criteriaBuilder.between(root.get("date"), startDate, endDate));
//
//            Path<LocalDate> datePath = root.get("date");
//            Path<LocalTime> timePath = root.get("time");
//            Order dateOrder = criteriaBuilder.asc(datePath);
//            Order timeOrder = criteriaBuilder.asc(timePath);
//            criteriaQuery.orderBy(dateOrder, timeOrder);
//
//            TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);
//            query.setFirstResult(0);
//            query.setMaxResults(pageRequest.getPageSize());
//
//            Iterator<DemoCSV> iterator = query.getResultList().iterator();
//            demoCSVIterators.add(page,iterator);
//            localDate = startDate.plusDays(280); // Adjust based on your requirement
//            System.out.println(localDate);
//            page++;
//        }

        return new ItemReader<DemoCSV>() {
            @Override
            public DemoCSV read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                for (Iterator<DemoCSV> demoCSVIterator : demoCSVIterators) {
                    if (demoCSVIterator.hasNext()) {
                        return demoCSVIterator.next();
                    }
                }
                return null;
            }
        };

    }

    @Bean
    @StepScope
    public FlatFileItemWriter<DemoCSV> exportItemWriter(@Value("#{jobParameters[path]}") String filePath){
        FlatFileItemWriter<DemoCSV> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(filePath)); // Update the file path as needed
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<DemoCSV>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<DemoCSV>() {
                    {
                        setNames(new String[]{"date", "time", /* Add other fields as needed */});
                    }
                });
            }
        });
        return writer;
    }
}