package com.vaistra.controllers;

import com.vaistra.dto.HttpResponse;
import com.vaistra.services.DemoService;
import com.vaistra.services.export.PdfGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    private final JobLauncher ExportPDFjobLauncher;
    private final Job job;
    private final Job exportPDFJob;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @Autowired
    public DemoController(DemoService demoService, JobLauncher jobLauncher, JobLauncher exportPDFjobLauncher, @Qualifier("exportDemoJob") Job job, @Qualifier("exportPDF") Job exportPDFJob) {
        this.demoService = demoService;
        this.jobLauncher = jobLauncher;
        ExportPDFjobLauncher = exportPDFjobLauncher;
        this.job = job;
        this.exportPDFJob = exportPDFJob;
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
    public ResponseEntity<Resource> exportCSV(@PathVariable String date1,
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
    public static File tempFile;

    @GetMapping(value ="/export/pdf/{date1}/{date2}")
    public ResponseEntity<Resource> exportToPDF(HttpServletResponse response,
                                                @PathVariable String date1,
                                                @PathVariable String date2) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        tempFile = File.createTempFile(LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter) + "demo_PDF_export", ".pdf");

        JobParameters jobParametersPDF = new JobParametersBuilder()
                .addString("pdfPath",tempFile.getAbsolutePath())
                .addString("date1", date1)
                .addString("date2", date2)
                .toJobParameters();

//        Resource fileResource = new FileSystemResource(tempFile.getAbsolutePath());

        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=" +tempFile.getAbsolutePath() + ".pdf";
        response.setHeader(headerkey, headervalue);

        System.out.println(tempFile.getAbsolutePath());

        JobExecution jobExecution = ExportPDFjobLauncher.run(exportPDFJob, jobParametersPDF);

        if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)){
            System.out.println("Job is Completed");
            PdfGenerator.downloadFle(response,tempFile.getAbsolutePath());
        }

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=PDF_data.pdf");

//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(fileResource.contentLength())
//                .contentType(MediaType.parseMediaType("application/pdf"))
//               .body(fileResource);
        return null;
    }
}
//    @GetMapping("getData")
//    public List<DemoCSV> getData(){
//        return demoService.temp();
//    }

