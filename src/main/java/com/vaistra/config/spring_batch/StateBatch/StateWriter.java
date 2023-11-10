package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.StateRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StateWriter implements ItemWriter<State> {
    @Getter
    @Setter
    private static long counter = 0;
    @Autowired
    private StateRepository stateRepository;

    @Override
    public void write(Chunk<? extends State> chunk) throws Exception {
        synchronized (this) {
            for (State state: chunk) {
                if(!stateRepository.existsByStateNameIgnoreCase(state.getStateName())){
                    stateRepository.save(state);
                    counter++;
                }
            }
        }
    }
}