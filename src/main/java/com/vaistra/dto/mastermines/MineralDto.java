package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MineralDto {

    private Integer mineralId;

    @NotEmpty(message = "Mineral name should not be empty!")
    @NotNull(message = "Mineral name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Mineral name must contain only alphabets with at least 3 characters!")
    private String mineralName;

    @NotEmpty(message = "Category should not be empty!")
    @NotNull(message = "Category should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Category name must contain only alphabets with at least 3 characters!")
    private String category;

    @NotEmpty(message = "ATR name should not be empty!")
    @NotNull(message = "ATR name should not be null!")
    private String atrName;

    @NotEmpty(message = "HSN code should not be Empty!")
    @NotNull(message = "HSN code should not be null!")
    @Pattern(regexp = "^\\d{6}(\\d{2})?$", message = "Invalid HSN Code.")
    @Size(min = 6, max = 8, message = "HSN code should have a length between 6 and 8 characters.")
    private String hsnCode;

    private String[] grade;
}
