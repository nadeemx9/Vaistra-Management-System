package com.vaistra.dto;

import com.vaistra.entities.cscv.State;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageListResponse {
    private boolean success;
    private String message;
    Map<Integer, String[]> data  = new HashMap<>();
}