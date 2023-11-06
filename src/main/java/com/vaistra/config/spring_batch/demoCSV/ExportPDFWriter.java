package com.vaistra.config.spring_batch.demoCSV;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.vaistra.controllers.DemoController;
import com.vaistra.entities.DemoCSV;
import com.vaistra.services.export.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.awt.Color.BLACK;

@Slf4j
public class ExportPDFWriter implements ItemWriter<DemoCSV> {

    String exportFilePath;

    public ExportPDFWriter(String path){
        exportFilePath = path;
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss");
    String currentDateTime = dateFormat.format(new Date());
    PdfGenerator generator = new PdfGenerator(exportFilePath, currentDateTime);
    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        generator.generate(chunk);
    }
}