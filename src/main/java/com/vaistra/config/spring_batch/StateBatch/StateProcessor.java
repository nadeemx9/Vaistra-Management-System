package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class StateProcessor implements ItemProcessor<State, State> {
    @Override
    public State process(State state) throws Exception {
        state.setStateName(state.getStateName());
        state.setStatus(true);
        state.setCountry(state.getCountry());
        return state;
    }
}