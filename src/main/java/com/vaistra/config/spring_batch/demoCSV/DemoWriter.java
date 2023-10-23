package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import com.vaistra.entities.cscv.Country;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.repositories.cscv.CountryRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DemoWriter implements ItemWriter<DemoCSV> {
    @Autowired
    private DemoRepository demoRepository;

    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        demoRepository.saveAll(chunk);
    }
}