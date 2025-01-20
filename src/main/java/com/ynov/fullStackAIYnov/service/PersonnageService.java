package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.dto.PersonnageDTO;
import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.repository.PersonnageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service pour la gestion des paramètres associés aux modèles de prompt et aux interactions.
 * Ce service fournit des méthodes pour récupérer, créer, mettre à jour et supprimer des objets
 * `Personnage`. Il interagit avec le `PersonnageRepository` pour effectuer des opérations sur les
 * données dans la base de données.
 *
 * <p>Méthodes :
 * 1. getParametersByPromptModelAndInteraction(Long promptModelId, Long interactionId) - Récupère les paramètres en fonction d'un modèle de prompt et d'une interaction.
 * 2. save(Personnage parameters) - Sauvegarde un nouveau paramètre dans la base de données.
 * 3. updateParameter(Long parameterId, PersonnageDTO parameterDTO) - Met à jour un paramètre existant.
 * 4. deleteParameter(Long parameterId) - Supprime un paramètre à partir de son ID.
 * </p>
 */
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
     * Met à jour un paramètre existant à partir d'un DTO (Data Transfer Object).
     *
     * @param parameterId L'ID du paramètre à mettre à jour.
     * @param personnageDTO Le DTO contenant les nouvelles informations pour le paramètre.
     * @return Le paramètre mis à jour.
     * @throws IllegalArgumentException Si le paramètre n'est pas trouvé avec l'ID donné.
     */
    public Personnage updatePersonnage(Long parameterId, PersonnageDTO personnageDTO) {
        Personnage existingPersonnage = personnageRepository.findById(parameterId)
                .orElseThrow(() -> new IllegalArgumentException("Personnage introuvable"));
        existingPersonnage.setNomPersonnage(personnageDTO.nomPersonnage());
        existingPersonnage.setTraitsPersonnalite(personnageDTO.traitsPersonnalite());
        existingPersonnage.setDescription(personnageDTO.description());
        return personnageRepository.save(existingPersonnage);
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
