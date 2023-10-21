package com.vaistra.config.spring_batch.CountryBatch;

import com.vaistra.entities.cscv.Country;
import com.vaistra.repositories.cscv.CountryRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CountryWriter implements ItemWriter<Country> {
    @Getter
    @Setter
    private static long counter = 0;
    @Autowired
    private CountryRepository countryRepository;
    @Override
    public void write(Chunk<? extends Country> chunk) throws Exception {
        for (Country country: chunk) {
            if(!countryRepository.existsByCountryNameIgnoreCase(country.getCountryName())){
                countryRepository.save(country);
                counter++;
            }
        }
    }
}