package com.vaistra.dto.cscv;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistrictUpdateDto {

    private Integer districtId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "District name must contain only alphabets with at least 3 characters!")
    private String districtName;

    @Min(value = 1, message = "State ID must be a positive integer!")
    private Integer stateId;

    private String stateName;

    private String countryName;

    private Boolean status;

}
