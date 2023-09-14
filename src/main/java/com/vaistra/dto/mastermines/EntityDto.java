package com.vaistra.dto.mastermines;

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
public class EntityDto {

    private Integer entityId;

    @NotEmpty(message = "Entity Type should not be empty!")
    @NotNull(message = "Entity Type should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Entity name must contain only alphabets with at least 3 characters!")
    private String entityType;

    @NotEmpty(message = "Short Type should not be empty!")
    @NotNull(message = "Short name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Short name must contain only alphabets with at least 3 characters!")
    private String shortName;
}
