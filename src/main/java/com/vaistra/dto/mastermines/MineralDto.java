package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Mineral name should not be null!")
    private String mineralName;

    @NotNull(message = "Category should not be null!")
    private String category;

    @NotNull(message = "ATR name should not be null!")
    private String atrName;

    @NotNull(message = "HSN code should not be null!")
    @NotEmpty(message = "HSN code should not be Empty!")
    private String hsnCode;

    private String[] grade;
}
