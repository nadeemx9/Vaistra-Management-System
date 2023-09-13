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
public class VillageDto {

    private Integer villageId;

    @NotNull(message = "Village name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Village name must contain only alphabets with at least 3 characters!")
    private String villageName;

    @NotNull(message = "Sub-District ID should not be null!")
    @Min(value = 1, message = "Sub-District ID must be a positive integer!")
    private Integer subDistrictId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Sub-District name must contain only alphabets with at least 3 characters!")
    private String subDistrictName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "District name must contain only alphabets with at least 3 characters!")
    private String districtName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "State name must contain only alphabets with at least 3 characters!")
    private String stateName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Country name must contain only alphabets with at least 3 characters!")
    private String countryName;

    private boolean status;

}
