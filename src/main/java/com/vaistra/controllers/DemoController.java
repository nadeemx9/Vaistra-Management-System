package com.vaistra.controllers;

import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.config.spring_batch.demoCSV.GetRecordConfig;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.MessageResponse;
import com.vaistra.services.DemoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("demo")
public class DemoController {
    private final DemoService demoService;
    private final JobLauncher jobLauncher;
    private final Job job;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @Autowired
    public DemoController(DemoService demoService, JobLauncher jobLauncher,@Qualifier("exportDemoJob") Job job) {
        this.demoService = demoService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("csvImport")
    public String importCSV(@RequestParam(required = false) MultipartFile file) {
        return demoService.importData(file);
    }

    @GetMapping("getData")
    public HttpResponse importCSV1(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "2147483647", required = false) Integer pageSize,
                                   @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection,
                                   @RequestParam(value = "fromDate") String date1,
                                   @RequestParam(value = "toDate") String date2) {
        return demoService.showData(pageNumber, pageSize, sortDirection, date1, date2);
    }

    @GetMapping("exportData/{date1}/{date2}")
    public ResponseEntity<Resource> exportCSV(HttpServletResponse response,
                                              @PathVariable String date1,
                                              @PathVariable String date2) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        File tempFile = File.createTempFile(LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter) + "demo_export", ".csv");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("path",tempFile.getAbsolutePath())
                .addString("date1", date1)
                .addString("date2", date2)
                .toJobParameters();

        Resource fileResource = new FileSystemResource(tempFile.getAbsolutePath());

        System.out.println(tempFile.getAbsolutePath());

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

//        if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)){
//            if(tempFile.exists()){
//                if(tempFile.delete()){}
//            }
//            CountryWriter.setCounter(0);
//        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=exported_data.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileResource.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(fileResource);
    }

    @PostMapping("/launch-job")
    public ResponseEntity<byte[]> launchJob() throws IOException, JobExecutionException {
        return null;
    }

//    @GetMapping("/export/excel")
//    public ResponseEntity<MessageResponse> exportToExcel(HttpServletResponse response) throws IOException {
//        return new ResponseEntity<>(demoService.exportToExcel(response), HttpStatus.OK);
//
//    }
//
//    @GetMapping("/export/csv")
//
//    public ResponseEntity<String> exportToCSV(HttpServletResponse response) throws IOException {
////        return new ResponseEntity<>(userService.exportToCSV(response),HttpStatus.OK);
//        demoService.exportToCSV(response);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @GetMapping(value ="/export/pdf")
//
//    public ResponseEntity<MessageResponse> exportToPDF(HttpServletResponse response) throws IOException {
//        return new ResponseEntity<>( demoService.exportToPDF(response),HttpStatus.OK);
//
//
//    }
}
//    @GetMapping("getData")
//    public List<DemoCSV> getData(){
//        return demoService.temp();
//    }

