package com.vaistra.dto.mastermines;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityUpdateDto {

    private Integer entityId;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Entity name must contain only alphabets with at least 3 characters!")
    private String entityType;

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Short name must contain only alphabets with at least 3 characters!")
    private String shortName;
}
