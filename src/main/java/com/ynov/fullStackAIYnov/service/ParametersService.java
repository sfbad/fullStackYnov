package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.dto.ParameterDTO;
import com.ynov.fullStackAIYnov.model.Parameters;
import com.ynov.fullStackAIYnov.repository.ParametersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service pour la gestion des paramètres associés aux modèles de prompt et aux interactions.
 * Ce service fournit des méthodes pour récupérer, créer, mettre à jour et supprimer des objets
 * `Parameters`. Il interagit avec le `ParametersRepository` pour effectuer des opérations sur les
 * données dans la base de données.
 *
 * <p>Méthodes :
 * 1. getParametersByPromptModelAndInteraction(Long promptModelId, Long interactionId) - Récupère les paramètres en fonction d'un modèle de prompt et d'une interaction.
 * 2. save(Parameters parameters) - Sauvegarde un nouveau paramètre dans la base de données.
 * 3. updateParameter(Long parameterId, ParameterDTO parameterDTO) - Met à jour un paramètre existant.
 * 4. updateParametersForInteraction(Long promptModelId, Long interactionId, List<ParameterDTO> parameterDTOList) - Met à jour plusieurs paramètres associés à une interaction.
 * 5. deleteParameter(Long parameterId) - Supprime un paramètre à partir de son ID.
 * 6. deleteParameters(List<Long> parameterIds) - Supprime plusieurs paramètres en fonction de leurs IDs.
 * 7. findByNomPersonnage(String nomPersonnage) - Recherche un paramètre par le nom du personnage.
 * 8. getParametersByInteractionId(Long interactionId) - Récupère les paramètres associés à une interaction spécifique.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ParametersService {

    private final ParametersRepository parametersRepository;

    /**
     * Récupère tous les paramètres associés à un modèle de prompt et une interaction spécifique.
     *
     * @param promptModelId L'ID du modèle de prompt.
     * @param interactionId L'ID de l'interaction.
     * @return La liste des paramètres associés au modèle de prompt et à l'interaction spécifiés.
     */
    public List<Parameters> getParametersByPromptModelAndInteraction(Long promptModelId, Long interactionId) {
        return parametersRepository.findAllByModelPrompted_IdAndHistoriqueInteraction_Id(promptModelId, interactionId);
    }

    /**
     * Sauvegarde un paramètre dans la base de données.
     *
     * @param parameters L'objet Parameters à sauvegarder.
     * @return Le paramètre sauvegardé.
     */
    public Parameters save(Parameters parameters) {
        return parametersRepository.save(parameters);
    }

    /**
     * Met à jour un paramètre existant à partir d'un DTO (Data Transfer Object).
     *
     * @param parameterId L'ID du paramètre à mettre à jour.
     * @param parameterDTO Le DTO contenant les nouvelles informations pour le paramètre.
     * @return Le paramètre mis à jour.
     * @throws IllegalArgumentException Si le paramètre n'est pas trouvé avec l'ID donné.
     */
    public Parameters updateParameter(Long parameterId, ParameterDTO parameterDTO) {
        Parameters existingParameter = parametersRepository.findById(parameterId)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre introuvable"));
        existingParameter.setNomPersonnage(parameterDTO.nomPersonnage());
        existingParameter.setTraitsPersonnalite(parameterDTO.traitsPersonnalite());
        existingParameter.setDescription(parameterDTO.description());
        return parametersRepository.save(existingParameter);
    }

    /**
     * Met à jour plusieurs paramètres associés à une interaction spécifique, en utilisant une liste de DTOs.
     *
     * @param promptModelId L'ID du modèle de prompt.
     * @param interactionId L'ID de l'interaction.
     * @param parameterDTOList La liste des DTOs contenant les nouvelles données des paramètres.
     */
    public void updateParametersForInteraction(Long promptModelId, Long interactionId, List<ParameterDTO> parameterDTOList) {
        List<Parameters> parameters = parametersRepository.findAllByModelPrompted_IdAndHistoriqueInteraction_Id(promptModelId, interactionId);
        for (Parameters param : parameters) {
            ParameterDTO dto = parameterDTOList.stream()
                    .filter(p -> p.nomPersonnage().equals(param.getNomPersonnage()))
                    .findFirst()
                    .orElse(null);

            if (dto != null) {
                param.setTraitsPersonnalite(dto.traitsPersonnalite());
                param.setDescription(dto.description());
                parametersRepository.save(param);
            }
        }
    }

    /**
     * Supprime un paramètre en fonction de son ID.
     *
     * @param parameterId L'ID du paramètre à supprimer.
     * @throws IllegalArgumentException Si le paramètre n'est pas trouvé avec l'ID donné.
     */
    public void deleteParameter(Long parameterId) {
        Parameters existingParameter = parametersRepository.findById(parameterId)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre introuvable"));
        parametersRepository.delete(existingParameter);
    }

    /**
     * Supprime plusieurs paramètres en fonction de leurs IDs.
     *
     * @param parameterIds La liste des IDs des paramètres à supprimer.
     */
    public void deleteParameters(List<Long> parameterIds) {
        List<Parameters> parameters = (List<Parameters>) parametersRepository.findAllById(parameterIds);
        parametersRepository.deleteAll(parameters);
    }

    /**
     * Recherche un paramètre par le nom du personnage.
     *
     * @param nomPersonnage Le nom du personnage à rechercher.
     * @return Le paramètre correspondant au nom du personnage, ou `null` si aucun paramètre n'est trouvé.
     */
    public Parameters findByNomPersonnage(String nomPersonnage){
        return parametersRepository.findByNomPersonnage(nomPersonnage).isPresent() ? parametersRepository.findByNomPersonnage(nomPersonnage).get() : null;
    }

    /**
     * Récupère tous les paramètres associés à une interaction spécifique.
     *
     * @param interactionId L'ID de l'interaction.
     * @return La liste des paramètres associés à l'interaction spécifiée.
     */
    public List<Parameters> getParametersByInteractionId(Long interactionId) {
        return parametersRepository.findAllByHistoriqueInteraction_Id(interactionId);
    }
}
