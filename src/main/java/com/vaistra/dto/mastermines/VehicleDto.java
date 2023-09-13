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
public class VehicleDto {

    private Integer vehicleId;

    @NotNull(message = "Vehicle name should not be null!")
    private String vehicleName;
}
