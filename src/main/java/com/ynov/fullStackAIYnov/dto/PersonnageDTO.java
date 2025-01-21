package com.ynov.fullStackAIYnov.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record PersonnageDTO(
        Long id,
        @NotNull(message = "Le nom est obligatoire")
                             String nomPersonnage,
                            @NotNull(message = "Au moins un trait de personalité doit etre renseigné")
                             List<String> traitsPersonnalite,
                            String description
                             )
{

}
