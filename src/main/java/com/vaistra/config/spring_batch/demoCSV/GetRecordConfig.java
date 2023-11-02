package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Configuration
@EnableBatchProcessing
public class GetRecordConfig {

    @PersistenceContext
    private EntityManager entityManager;
    @Bean
    @Qualifier("exportDemoJob")
    public Job exportDemoJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemWriter<DemoCSV> writer, ItemReader<DemoCSV> reader){
        return new JobBuilder("exportDemoJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exportChunkStepdemo(jobRepository,transactionManager,writer,reader))
                .build();
    }

    @Bean
    public Step exportChunkStepdemo(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemWriter<DemoCSV> writer, ItemReader<DemoCSV> reader){
        return new StepBuilder("countryReaderStep",jobRepository)
                .<DemoCSV, DemoCSV>chunk(10000,transactionManager)
                .writer(writer)
                .reader(reader)
                .listener(zipCsvFileTasklet())
                .allowStartIfComplete(true)
                .taskExecutor(exportTaskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor exportTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10); // Set the number of concurrent threads
        taskExecutor.setMaxPoolSize(20); // Set the maximum number of threads
        taskExecutor.setQueueCapacity(30); // Set the queue capacity for pending tasks
        return taskExecutor;
    }

    public ItemReader<DemoCSV> reader(@Value("#{jobParameters[date1]}") String date1,@Value("#{jobParameters[date2]}") String date2){
        return new ItemReader<DemoCSV>() {
            private int page = 0;
            private int pageSize = 10000;
            @Override
            public DemoCSV read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate startDate = LocalDate.parse(date1, formatter);
                LocalDate endDate = LocalDate.parse(date2, formatter);

                // Create a query to retrieve data for the current page
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
                Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);
                criteriaQuery.select(root)
                        .where(criteriaBuilder.between(root.get("date"), startDate, endDate));

                // Define sorting based on date and time
                Path<LocalDate> datePath = root.get("date");
                Path<LocalTime> timePath = root.get("time");
                Order dateOrder = criteriaBuilder.asc(datePath);
                Order timeOrder = criteriaBuilder.asc(timePath);
                criteriaQuery.orderBy(dateOrder, timeOrder);

                TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);
                query.setFirstResult(page * pageSize);
                query.setMaxResults(pageSize);

                List<DemoCSV> pageData = query.getResultList();

                if (pageData.isEmpty()) {
                    // No more data to read
                    return null;
                }

                // Increment the page counter for the next read
                page++;

                // Return the current item
                return pageData.get(0);

            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<DemoCSV> exportItemWriter(){



        return null;
    }

    @Bean
    public StepExecutionListener zipCsvFileTasklet() {
        return new StepExecutionListener() {
            public void beforeStep(StepExecution stepExecution) {
                // Implement logic before executing the tasklet step
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                // Implement the logic to zip the CSV file here
                try {
                    // Create the zip file and add the CSV file to it
                    String outputCsvPath = "output.csv"; // Specify the path to the CSV file
                    String outputZipPath = "output.zip"; // Specify the path for the zip file

                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZipPath));
                         FileInputStream csvInputStream = new FileInputStream(outputCsvPath)) {
                        ZipEntry zipEntry = new ZipEntry("output.csv");
                        zipOutputStream.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = csvInputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, len);
                        }

                        zipOutputStream.closeEntry();
                    }

                    // Optionally, you can delete the CSV file if it's no longer needed
                    File csvFile = new File(outputCsvPath);
                    if (csvFile.delete()) {
                        System.out.println("CSV file deleted.");
                    } else {
                        System.out.println("Failed to delete CSV file.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error zipping CSV file");
                }

                return ExitStatus.COMPLETED;
            }
        };
    }
}