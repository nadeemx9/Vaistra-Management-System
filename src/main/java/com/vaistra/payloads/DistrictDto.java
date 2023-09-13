package com.vaistra.payloads;

import com.vaistra.entities.State;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDto {

    private Integer districtId;

    @NotNull(message = "District name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "District name must contain only alphabets with at least 3 characters!")
    private String districtName;

    @NotNull(message = "State ID should not be null!")
    @Min(value = 1, message = "State ID must be a positive integer!")
    private Integer stateId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "State name must contain only alphabets with at least 3 characters!")
    private String stateName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Country name must contain only alphabets with at least 3 characters!")
    private String countryName;

    private boolean status;

}
