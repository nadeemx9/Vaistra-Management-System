package com.vaistra.config.spring_batch.StateBatch;

import com.vaistra.entities.cscv.State;
import com.vaistra.repositories.cscv.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StateWriter{

//    @Autowired
//    private StateRepository stateRepository;
//
//    @Override
//    public void write(Chunk<? extends State> chunk) throws Exception {
////        if(!stateRepository.existsByStateNameIgnoreCase(chunk.iterator().next().getStateName()))
//            stateRepository.saveAll(chunk);
//    }
}