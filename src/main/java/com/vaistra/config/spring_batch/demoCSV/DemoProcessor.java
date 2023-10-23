package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import com.vaistra.entities.cscv.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class DemoProcessor implements ItemProcessor<DemoCSV,DemoCSV> {

    @Override
    public DemoCSV process(DemoCSV demo) throws Exception {
        demo.setDate(demo.getDate());
        demo.setTime(demo.getTime());
        return demo;
    }
}
