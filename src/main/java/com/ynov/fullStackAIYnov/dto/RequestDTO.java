package com.ynov.fullStackAIYnov.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record RequestDTO(@NotNull List<PersonnageDTO> personnageDTOS,
                         @NotNull(message = " Veuillez remplir le trame de l'histoire")
                         String tramHistoire
                            ) {

    public StringBuilder getTramHistoireWithDetails() {

        StringBuilder tramHistoireWithDetails = new StringBuilder();

        tramHistoireWithDetails.append("Écris un scénario avec la trame d'histoire suivante :\n")
                .append(tramHistoire).append("\n\n")
                .append("Les personnages sont les suivants :\n");

        personnageDTOS.forEach(personnageDTO -> {

            String description = personnageDTO.description();

            tramHistoireWithDetails.append("Le personnage \"")
                    .append(personnageDTO.nomPersonnage()).append("\" a les traits de personnalité suivants : ")
                    .append(String.join(", ", personnageDTO.traitsPersonnalite()));
            if (description != null) {
                tramHistoireWithDetails.append(". Description : ").append(description);
            }

            tramHistoireWithDetails.append("\n");
        });

        return tramHistoireWithDetails;
    }
}
