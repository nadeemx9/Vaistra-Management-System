package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "Sub-District name Should not be Empty!")
    @NotBlank(message = "Sub-District name Should not be Blank!")
    @Size(min = 3, message = "Sub-District name should be at least 3 characters!")
    private String subDistrictName;

    @NotNull(message = "District ID should not be null!")
    private Integer districtId;

    private boolean status;

}
