package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.controllers.DemoController;
import com.vaistra.entities.DemoCSV;
import com.vaistra.services.export.ExcelGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class ExportExcelWriter implements ItemWriter<DemoCSV> {

    private final ExcelGenerator excelGenerator;

    public ExportExcelWriter() {
        excelGenerator = new ExcelGenerator();
    }
    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        System.out.println("In Writer Method");
        excelGenerator.export(chunk);
    }
}