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
public class DesignationDto {

    private Integer designationId;

    @NotNull(message = "Designation Type should not be null!")
    private String designationType;
}
