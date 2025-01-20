package com.ynov.fullStackAIYnov.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record PersonnageDTO(@NotNull(message = "Le nom est obligatoire")
                             String nomPersonnage,
                            @NotNull(message = "Au moins un trait de personalité doit etre renseigné")
                             List<String> traitsPersonnalite,
                            String description
                             )
{

}
