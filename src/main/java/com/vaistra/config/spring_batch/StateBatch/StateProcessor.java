package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.dto.cscv.StateDto;
import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.CountryRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateProcessor implements ItemProcessor<StateDto, State> {
    @Getter
    @Setter
    private static Map<Integer,String[]> countries = new HashMap<>();
    private final CountryRepository countryRepository;
    @Autowired
    public StateProcessor(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    int counter = 0;
    @Override
    public State process(StateDto item) throws Exception {
        State state = new State();
        state.setStateName(item.getStateName());
        state.setStatus(item.getStatus());
        Country country = countryRepository.findByCountryNameIgnoreCase(item.getCountryName());
        if(country != null)
            state.setCountry(country);
        else {
            countries.put(counter++,new String[]{item.getCountryName(),item.getStateName()});
        }
        return state;
    }
}