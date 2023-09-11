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
public class StateDto {
    private Integer stateId;

    @NotEmpty(message = "State name Should not be Empty!")
    @NotBlank(message = "State name Should not be Blank!")
    @Size(min = 3, message = "State name should be at least 3 characters!")
    private String stateName;

    private boolean status;

    @NotNull(message = "Country ID should not be null!")
    private Integer countryId;

}
