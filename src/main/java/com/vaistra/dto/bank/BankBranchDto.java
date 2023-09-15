package com.vaistra.dto.bank;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankBranchDto {
    private Integer BranchId;

    @NotEmpty(message = "Branch name should not be empty!")
    @NotNull(message = "Branch name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Branch name must contain only alphabets with at least 3 characters!")
    private String branchName;

    @NotEmpty(message = "Branch code should not be empty!")
    @NotNull(message = "Branch code should not be null!")
    private String branchCode;

    @NotEmpty(message = "Branch address should not be empty!")
    @NotNull(message = "Branch address should not be null!")
    private String branchAddress;

    @NotEmpty(message = "Branch IFSC should not be empty!")
    @NotNull(message = "Branch IFSC should not be null!")
    private String branchIfsc;

//    @NotEmpty(message = "Branch Phone number should not be empty!")
//    @NotNull(message = "Branch Phone number should not be null!")
//    @Min(value = 1, message = "Invalid Phone Number")
    private String branchPhoneNumber;

//    @NotEmpty(message = "Branch MICR should should not be empty!")
//    @NotNull(message = "Branch MICR should not be null!")
    private String branchMicr;

//    @NotEmpty(message = "Branch from timing should not be empty!")
//    @NotNull(message = "Branch from timing should not be null!")
    private LocalTime fromTiming;

//    @NotEmpty(message = "Branch to timing should not be empty!")
//    @NotNull(message = "Branch to timing should not be null!")
    private LocalTime toTiming;

    @NotEmpty(message = "Bank ID should not be empty!")
    @NotNull(message = "Bank ID should not be null!")
    @Min(value = 1, message = "Bank ID should be a positive numeric value!")
    private Integer bankId;

    @NotEmpty(message = "District ID should not be empty!")
    @NotNull(message = "District ID should not be null!")
    @Min(value = 1, message = "District ID should be a positive numeric value!")
    private Integer districtId;

    private Boolean status;
}