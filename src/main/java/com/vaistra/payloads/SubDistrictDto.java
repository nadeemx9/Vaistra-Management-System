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

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Sub-District name must contain only alphabets with at least 3 characters!")
    private String subDistrictName;

    @NotNull(message = "District ID should not be null!")
    @Min(value = 1, message = "District ID must be a positive integer!")
    private Integer districtId;

    private boolean status;

}
