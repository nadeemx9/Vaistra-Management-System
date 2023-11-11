package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.dto.cscv.StateDto;
import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.StateRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateProcessor implements ItemProcessor<StateDto, State> {
    private final CountryRepository countryRepository;

    // Initialize the processed country names set.
    private static final Map<String, Country> countries = new HashMap<>();

    @Autowired
    public StateProcessor(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public State process(StateDto item) throws Exception {
        State state = new State();
        Country country;
        synchronized (countries) {
            country = countries.get(item.getCountryName());
            if (country == null) {
                country = countryRepository.findByCountryNameIgnoreCase(item.getCountryName());
                if (country == null) {
                    country = new Country();
                    country.setCountryName(item.getCountryName());
                    country.setStatus(true);
                    countries.put(item.getCountryName(), country);
                    countryRepository.saveAndFlush(countries.get(item.getCountryName()));
                }
            }
            state.setCountry(countries.get(item.getCountryName()));
            state.setStateName(item.getStateName());
            state.setStatus(item.getStatus());
            return state;
        }
    }
}