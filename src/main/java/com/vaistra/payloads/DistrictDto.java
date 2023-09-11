package com.vaistra.payloads;

import com.vaistra.entities.State;
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
public class DistrictDto {

    private Integer districtId;

    @NotEmpty(message = "District name Should not be Empty!")
    @NotBlank(message = "District name Should not be Blank!")
    @Size(min = 3, message = "District name should be at least 3 characters!")
    private String districtName;

    private boolean status;

    @NotNull(message = "State ID should not be null!")
    private Integer stateId;

}
