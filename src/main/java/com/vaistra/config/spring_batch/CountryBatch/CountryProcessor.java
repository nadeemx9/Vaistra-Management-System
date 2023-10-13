package com.vaistra.config.spring_batch.CountryBatch;

import com.vaistra.entities.cscv.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CountryProcessor implements ItemProcessor<Country,Country> {

    @Override
    public Country process(Country country) throws Exception {
        country.setCountryName(country.getCountryName().trim());
        country.setStatus(true);
        return country;
    }
}