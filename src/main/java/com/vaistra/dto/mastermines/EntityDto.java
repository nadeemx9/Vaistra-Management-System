package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityDto {

    private Integer entityId;

    @NotNull(message = "Entity Type should not be null!")
    private String entityType;

    @NotNull(message = "Entity short name should not be null!")
    private String shortName;
}
