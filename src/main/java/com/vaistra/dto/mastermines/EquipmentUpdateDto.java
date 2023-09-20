package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUpdateDto {

    private Integer equipmentId;

    @NotNull(message = "Equipment name should not be null!")
    @NotEmpty(message = "Equipment name should not be empty!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Equipment name must contain only alphabets with at least 3 characters!")
    private String equipmentName;
}
