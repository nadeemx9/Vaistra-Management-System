package com.vaistra.services;

import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.utils.AppUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class DemoService {
    private final DemoRepository demoRepository;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final AppUtils appUtils;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @Autowired
    public DemoService(DemoRepository demoRepository, JobLauncher jobLauncher, @Qualifier("demoReaderJob")  Job job, AppUtils appUtils) {
        this.demoRepository = demoRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.appUtils = appUtils;
    }

    public String importData(MultipartFile file) {
        if(file == null)
            throw new ResourceNotFoundException("CSV File is not Uploaded   ");
        if(file.isEmpty())
            throw new ResourceNotFoundException("Country CSV File not found...!");
        if(!Objects.equals(file.getContentType(), "text/csv"))
            throw new IllegalArgumentException("Invalid file type. Please upload a CSV file.");
        if(!appUtils.isSupportedExtensionBatch(file.getOriginalFilename()))
            throw new ResourceNotFoundException("Only CSV and Excel File is Accepted");

        try {
            File tempFile = File.createTempFile(LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter) + "_Country_" +"temp", ".csv");
            String orignalFileName = file.getOriginalFilename();
            assert orignalFileName != null;
            file.transferTo(tempFile);


            System.out.println(tempFile.getAbsolutePath());

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("inputFileDemo", tempFile.getAbsolutePath())
                    .toJobParameters();

            JobExecution execution =  jobLauncher.run(job, jobParameters);

            if (execution.getExitStatus().equals(ExitStatus.COMPLETED)){
                System.out.println("Job is Completed");
                if(tempFile.exists()){
                    if(tempFile.delete())
                        System.out.println("File Deleted");
                    else
                        System.out.println("Can't Delete File");
                }
                CountryWriter.setCounter(0);
            }

            return "CSV file uploaded successfully.";

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}