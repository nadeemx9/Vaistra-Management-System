package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MineralUpdateDto {
    private Integer mineralId;

    private String mineralName;

    private String category;

    private String atrName;

    private String hsnCode;

    private String[] grade;
}
