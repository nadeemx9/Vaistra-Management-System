package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.StateRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StateWriter implements ItemWriter<State> {
    @Getter
    @Setter
    private static long insertedCounter = 0;
    @Getter
    @Setter
    private static long FailedCounter = 0;

    @Autowired
    private StateRepository stateRepository;

    @Override
    public void write(Chunk<? extends State> chunk) throws Exception {
        for (State state : chunk) {
            if (!stateRepository.existsByStateNameIgnoreCase(state.getStateName())) {
                synchronized (this) {
                    if (state.getCountry() == null)
                        FailedCounter++;
                    else {
                        stateRepository.save(state);
                        insertedCounter++;
                    }
                }
            }
        }
    }
}