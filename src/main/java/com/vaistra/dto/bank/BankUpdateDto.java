package com.vaistra.dto.bank;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankUpdateDto {
    private Integer bankId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Bank short name must contain only alphabets with at least 3 characters!")
    private String bankShortName;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Bank long name must contain only alphabets with at least 3 characters!")
    private String bankLongName;

    private String bankLogo;

    private Boolean status;
}
