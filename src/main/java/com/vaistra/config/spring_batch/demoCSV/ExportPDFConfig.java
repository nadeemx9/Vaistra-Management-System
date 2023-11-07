package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ExportPDFConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    @Qualifier("exportPDF")
    public Job exportPDFJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemWriter<DemoCSV> exportPDFWriter, ItemReader<DemoCSV> exportPDFReader){
        return new JobBuilder("exportPDFJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exportChunkPDF(jobRepository,transactionManager,exportPDFWriter,exportPDFReader))
                .build();
    }

    @Bean
    public Step exportChunkPDF(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemWriter<DemoCSV> exportPDFWriter, ItemReader<DemoCSV> exportPDFReader){
        return new StepBuilder("exportChunkPDF",jobRepository)
                .<DemoCSV, DemoCSV>chunk(50000,transactionManager)
                .writer(exportPDFWriter)
                .reader(exportPDFReader)
                .allowStartIfComplete(true)
//                .taskExecutor(exportPDFTaskExecutor())
                .build();
    }

//    @Bean
//    public TaskExecutor exportPDFTaskExecutor(){
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(15); // Set the number of concurrent threads
//        taskExecutor.setMaxPoolSize(25); // Set the maximum number of threads
//        taskExecutor.setQueueCapacity(50); // Set the queue capacity for pending tasks
//        return taskExecutor;
//    }

    static LocalDate localDate;
    static LocalDate date;

    @Bean
    @StepScope
    public ItemReader<DemoCSV> exportPDFReader(@Value("#{jobParameters[date1]}") String date1, @Value("#{jobParameters[date2]}") String date2){
        int pageSize = 403200;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        List<Iterator<DemoCSV>> demoCSVIterators = new ArrayList<>();

        int page = 0;

        localDate = startDate;

        do{
            date = localDate.plusDays(280);

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
            Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.between(root.get("date"), localDate,date.isAfter(endDate)? endDate : date.isEqual(endDate) ? endDate : date)); // 1-1-2010 31-12-2030

            TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);

            Iterator<DemoCSV> iterator = query.getResultList().iterator();
            demoCSVIterators.add(iterator);
            localDate = localDate.plusDays(280);
            System.out.println(localDate); // 08-10-2010 11-3-2011
            page++;
            System.out.println(page);
        }while (localDate.isBefore(endDate.plusDays(1))); // 60,48,000 enough

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
    public ItemWriter<DemoCSV> exportPDFWriter(@Value("#{jobParameters[pdfPath]}") String filePath){
        return new ExportPDFWriter(filePath);
    }
}