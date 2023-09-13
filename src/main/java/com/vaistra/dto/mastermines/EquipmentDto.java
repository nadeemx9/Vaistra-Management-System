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
public class EquipmentDto {

    private Integer equipmentId;

    @NotNull(message = "Equipment name should not be null!")
    private String equipmentName;
}
