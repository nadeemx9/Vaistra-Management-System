package com.vaistra.dto.bank;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankBranchUpdateDto {
    private Integer branchId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Branch name must contain only alphabets with at least 3 characters!")
    private String branchName;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Branch Code must contain only alphanumeric character with no space!")
    private String branchCode;

    private String branchAddress;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "IFSC should must contain only alphanumeric character with no space!")
    @Size(min = 10, message = "IFSC should contain at least 10 alphanumeric characters!")
    private String branchIfsc;

    @Pattern(regexp = "^[0-9+\\s]+$", message = "Phone Number should only contain numeric, '+', and spaces.")
    @Size(min = 10, max = 15, message = "Phone Number should have a length between 10 and 15 digits.")
    private String branchPhoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "MICR should must contain only alphanumeric character with no space!")
    private String branchMicr;

    private LocalTime fromTiming;

    private LocalTime toTiming;

    @Min(value = 1, message = "Bank ID should be a positive numeric value!")
    private Integer bankId;

    @Min(value = 1, message = "District ID should be a positive numeric value!")
    private Integer districtId;

    private Boolean status;
}
