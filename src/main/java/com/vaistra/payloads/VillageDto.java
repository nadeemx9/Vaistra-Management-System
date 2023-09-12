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

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Village name must contain only alphabets with at least 3 characters!")
    private String villageName;

    private boolean status;

    @Min(value = 1, message = "Sub-District ID must be a positive integer!")
    private Integer subDistrictId;
}
