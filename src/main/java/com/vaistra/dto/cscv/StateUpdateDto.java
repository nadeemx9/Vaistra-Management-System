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
public class StateUpdateDto {
    private Integer stateId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "State name must contain only alphabets with at least 3 characters!")
    private String stateName;

    @Min(value = 1, message = "Country ID must be a positive integer!")
    private Integer countryId;

    private String countryName;

    private Boolean status;
}
