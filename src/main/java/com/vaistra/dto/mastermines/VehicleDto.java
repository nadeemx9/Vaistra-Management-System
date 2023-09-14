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
public class VehicleDto {

    private Integer vehicleId;

    @NotNull(message = "Vehicle name should not be null!")
    @NotEmpty(message = "Vehicle name should not be empty!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Vehicle name must contain only alphabets with at least 3 characters!")
    private String vehicleName;
}
