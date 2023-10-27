package com.vaistra.services;

import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.entities.DemoCSV;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.utils.AppUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DemoService {
    private final DemoRepository demoRepository;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final Job job2;
    private final AppUtils appUtils;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @Autowired
    public DemoService(DemoRepository demoRepository, JobLauncher jobLauncher, @Qualifier("demoReaderJob")  Job job,@Qualifier("demoGetDataJob") Job job2, AppUtils appUtils) {
        this.demoRepository = demoRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.job2 = job2;
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

    public Page<DemoCSV> showData(int pageNumber, int pageSize, String sortBy, String sortDirection){
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<DemoCSV> demoCSV = demoRepository.findAll(pageable);
        Page<DemoCSV> responce;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date1 = LocalDate.of(1990,1,1);
        LocalDate date2 = LocalDate.of(2030,12,31);

//        for (DemoCSV csv: demoCSV) {
//            LocalDate recordDate = LocalDate.parse(csv.getDate(), formatter);
//            if(recordDate.isAfter(date1) && recordDate.isBefore(date2)){
//                System.out.println(csv.getDate());
//            }
//        }

//        List<DemoCSV> retrievedData =
//        return new ResponseEntity<>(retrievedData, HttpStatus.OK);
        return demoCSV;
    }

    public List<DemoCSV> temp(){
//        List<DemoCSV> demoCSVS = demoRepository.findAll();

//        List<DemoCSV> getByDate = demoRepository.findByDateBetween(LocalDate.of(2020,1,1),LocalDate.of(2021,12,31));

        LocalDate date1 = LocalDate.of(2000,1,1);
        LocalDate date2 = LocalDate.of(2006,12,31);

        List<DemoCSV> getByDate = demoRepository.findMinuteTimestamps(date1,date2);

        System.out.println("Data fetched by Query : "+getByDate.size());

        return getByDate;
    }
}