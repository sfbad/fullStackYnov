package com.ynov.fullStackAIYnov.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record RequestDTO(@NotNull List<ParameterDTO> parameterDTOS,
                         @NotNull(message = " Veuillez remplir le trame de l'histoire")
                         String tramHistoire
                            ) {

    public StringBuilder getTramHistoireWithDetails() {

        StringBuilder tramHistoireWithDetails = new StringBuilder();

        tramHistoireWithDetails.append("Écris un scénario avec la trame d'histoire suivante :\n")
                .append(tramHistoire).append("\n\n")
                .append("Les personnages sont les suivants :\n");

        parameterDTOS.forEach(parameterDTO -> {

            String description = parameterDTO.description();

            tramHistoireWithDetails.append("Le personnage \"")
                    .append(parameterDTO.nomPersonnage()).append("\" a les traits de personnalité suivants : ")
                    .append(String.join(", ", parameterDTO.traitsPersonnalite()));
            if (description != null) {
                tramHistoireWithDetails.append(". Description : ").append(description);
            }

            tramHistoireWithDetails.append("\n");
        });

        return tramHistoireWithDetails;
    }
}
