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
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.awt.Color.BLACK;

@Slf4j
public class ExportPDFWriter implements ItemWriter<DemoCSV> {
    private PdfGenerator generator;
    public ExportPDFWriter(String path) {
        generator = new PdfGenerator();
    }

    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        generator.generate(chunk);
    }

}