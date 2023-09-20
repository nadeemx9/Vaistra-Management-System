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
public class VillageUpdateDto {

    private Integer villageId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Village name must contain only alphabets with at least 3 characters!")
    private String villageName;

    @Min(value = 1, message = "Sub-District ID must be a positive integer!")
    private Integer subDistrictId;

    private String subDistrictName;

    private String districtName;

    private String stateName;

    private String countryName;

    private Boolean status;

}
