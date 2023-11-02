package com.vaistra.controllers;

import com.opencsv.CSVWriter;
import com.vaistra.dto.HttpResponse;
import com.vaistra.entities.DemoCSV;
import com.vaistra.services.DemoService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("demo")
public class DemoController {
    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
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
    public void exportCSV(HttpServletResponse response,
                                              @PathVariable String date1,
                                              @PathVariable String date2) throws IOException {

        // Set the content type and headers for CSV response
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");

        // Use a ServletOutputStream to write the CSV data directly to the response
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            demoService.getChunkedData(outputStream,date1,date2);
        } catch (Exception e) {
            // Handle exceptions, such as database or file I/O errors
            e.printStackTrace();
            // You might want to send an error response to the client
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/launch-job")
    public ResponseEntity<byte[]> launchJob() throws IOException, JobExecutionException {
        return null;
    }


}
//    @GetMapping("getData")
//    public List<DemoCSV> getData(){
//        return demoService.temp();
//    }

