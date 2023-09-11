package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private int countryId;

    @NotEmpty(message = "Country Should not be Empty!")
    @NotBlank(message = "Country Should not be Blank!")
    private String countryName;

    private boolean status;

}
