package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.cscvdb.entities.State;
import com.vaistra.cscvdb.repositories.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StateWriter implements ItemWriter<State> {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public void write(Chunk<? extends State> chunk) throws Exception {
        for (State state:chunk) {
            if(!stateRepository.existsByStateName(state.getStateName())){
                stateRepository.save(state);
            }
        }
    }
}