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
public class VillageDto {

    private Integer villageId;

    @NotEmpty(message = "Village name Should not be Empty!")
    @NotBlank(message = "Village name Should not be Blank!")
    @Size(min = 3, message = "Village name should be at least 3 characters!")
    private String villageName;

    private boolean status;

    @NotNull(message = "Sub-District ID should not be null!")
    private Integer subDistrictId;
}
