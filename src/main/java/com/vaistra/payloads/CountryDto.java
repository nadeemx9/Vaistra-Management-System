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
public class CountryDto {

    private Integer countryId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Country name must contain only alphabets with at least 3 characters")
    private String countryName;

    private boolean status;

}
