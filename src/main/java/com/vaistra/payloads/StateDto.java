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
public class StateDto {
    private Integer stateId;

    @NotNull(message = "State name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "State name must contain only alphabets with at least 3 characters!")
    private String stateName;

    @NotNull(message = "Country ID should not be null!")
    @Min(value = 1, message = "Country ID must be a positive integer!")
    private Integer countryId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Country name must contain only alphabets with at least 3 characters!")
    private String countryName;

    private boolean status;
}
