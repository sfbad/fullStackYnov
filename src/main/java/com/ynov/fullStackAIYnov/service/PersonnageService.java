package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.repository.PersonnageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonnageService {

    private final PersonnageRepository personnageRepository;

    /**
     * Récupère tous les paramètres associés à un modèle de prompt et une interaction spécifique.
     *
     * @param scenarioID L'ID de l'interaction.
     * @return La liste des paramètres associés au modèle de prompt et à l'interaction spécifiés.
     */
    public List<Personnage> getPersonnagesByScenarioID(Long scenarioID) {
        return personnageRepository.findByScenario_Id(scenarioID);
    }

    /**
     * Sauvegarde un paramètre dans la base de données.
     *
     * @param personnage L'objet Personnage à sauvegarder.
     * @return Le paramètre sauvegardé.
     */
    public Personnage save(Personnage personnage) {
        return personnageRepository.save(personnage);
    }

    /**
     * Supprime un paramètre en fonction de son ID.
     *
     * @param parameterId L'ID du paramètre à supprimer.
     * @throws IllegalArgumentException Si le paramètre n'est pas trouvé avec l'ID donné.
     */
    public void deletePersonnage(Long parameterId) {
        Personnage existingParameter = personnageRepository.findById(parameterId)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre introuvable"));
        personnageRepository.delete(existingParameter);
    }

}
