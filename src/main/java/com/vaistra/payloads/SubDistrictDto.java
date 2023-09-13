package com.vaistra.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubDistrictDto {

    private Integer subDistrictId;

    @NotNull(message = "Sub-District name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Sub-District name must contain only alphabets with at least 3 characters!")
    private String subDistrictName;

    @NotNull(message = "District ID should not be null!")
    @Min(value = 1, message = "District ID must be a positive integer!")
    private Integer districtId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "District name must contain only alphabets with at least 3 characters!")
    private String districtName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "State name must contain only alphabets with at least 3 characters!")
    private String stateName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Country name must contain only alphabets with at least 3 characters!")
    private String countryName;

    private boolean status;

}
