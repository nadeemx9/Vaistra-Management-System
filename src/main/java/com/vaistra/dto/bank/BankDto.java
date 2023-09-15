package com.vaistra.dto.bank;

import com.vaistra.entities.bank.BankBranch;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
public class BankDto {
    private Integer bankId;

    @NotEmpty(message = "Bank short name should not be empty!")
    @NotNull(message = "Bank short name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Bank short name must contain only alphabets with at least 3 characters!")
    private String bankShortName;

    @NotEmpty(message = "Bank long name should not be empty!")
    @NotNull(message = "Bank long name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Bank long name must contain only alphabets with at least 3 characters!")
    private String bankLongName;

    private Boolean status;
}
