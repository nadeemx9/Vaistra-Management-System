package com.vaistra.controllers;

import com.vaistra.entities.DemoCSV;
import com.vaistra.services.DemoService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("demo")
public class DemoController {
    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("csvImport")
    public String importCSV(@RequestParam(required = false) MultipartFile file){
        return demoService.importData(file);
    }

//    @GetMapping("getData")
//    public Page<DemoCSV> importCSV1(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
//                                   @RequestParam(value = "pageSize", defaultValue = "500", required = false) Integer pageSize,
//                                   @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
//                                   @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection){
//        return demoService.showData(pageNumber, pageSize, sortBy, sortDirection);
//    }

    @GetMapping("getData")
    public List<DemoCSV> getData(){
        return demoService.temp();
    }
}