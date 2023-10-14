package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StateProcessor implements ItemProcessor<State, State> {

    @Override
    public State process(State state) throws Exception {
        state.setStateName(state.getStateName());
        state.setStatus(state.getStatus());
        state.setCountry(state.getCountry());
        return state;
    }
}