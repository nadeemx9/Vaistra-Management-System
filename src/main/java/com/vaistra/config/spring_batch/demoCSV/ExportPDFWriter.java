package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import com.vaistra.services.export.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

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