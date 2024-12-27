package com.ynov.fullStackAIYnov.controller;

import com.ynov.fullStackAIYnov.dto.ParameterDTO;
import com.ynov.fullStackAIYnov.dto.PromptModelDTO;
import com.ynov.fullStackAIYnov.dto.RequestDTO;
import com.ynov.fullStackAIYnov.model.HistoriqueInteraction;
import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.model.Parameters;
import com.ynov.fullStackAIYnov.repository.HistoriqueInteractionRepository;
import com.ynov.fullStackAIYnov.repository.PromptModelRepository;
import com.ynov.fullStackAIYnov.service.DiscussionService;
import com.ynov.fullStackAIYnov.service.PromptModelService;
import com.ynov.fullStackAIYnov.service.ParametersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatManagementController {

    private final DiscussionService discussionService;
    private final PromptModelRepository promptModelRepository;
    private final ParametersService parametersService;

    /**
     * Requête pour obtenir une réponse de l'IA à partir de la trame et des paramètres.
     *
     * @param promptId ID du modèle de prompt utilisé pour la réponse.
     * @param requestDTO Contient la question de l'utilisateur et les paramètres associés.
     * @return La réponse de l'IA.
     */
    @PostMapping("/{promptId}/ask")
    public ResponseEntity<String> request(@PathVariable Long promptId, @RequestBody RequestDTO requestDTO) {
        try {
            // Vérification de l'existence du modèle de prompt
            PromptModel promptModel = promptModelRepository.findById(promptId)
                    .orElseThrow(() -> new IllegalArgumentException("PromptModel not found with ID: " + promptId));

            // Traitement de l'interaction avec l'IA
            String response = discussionService.processInteraction(promptId, requestDTO);
            return ResponseEntity.ok(response);

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body("Une erreur est survenue : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtenir un modèle de prompt par son ID.
     *
     * @param id ID du modèle de prompt à récupérer.
     * @return Le modèle de prompt sous forme de DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromptModelDTO> getPromptById(@PathVariable Long id) {
        return promptModelRepository.findById(id)
                .map(prompt -> ResponseEntity.ok(PromptModelDTO.builder()
                        .description(prompt.getDescription())
                        .filepath(prompt.getFilepath())
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupérer les paramètres pour un modèle de prompt et une interaction spécifiques.
     *
     * @param promptId ID du modèle de prompt.
     * @param interactionId ID de l'interaction.
     * @return Liste des paramètres associés au modèle de prompt et à l'interaction.
     */
    @GetMapping("/{promptId}/interaction/{interactionId}/parameters")
    public ResponseEntity<List<Parameters>> getParametersForPromptAndInteraction(
            @PathVariable Long promptId,
            @PathVariable Long interactionId
    ) {
        try {
            List<Parameters> parameters = parametersService.getParametersByPromptModelAndInteraction(promptId, interactionId);
            return ResponseEntity.ok(parameters);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Ajouter ou mettre à jour les paramètres pour un modèle de prompt.
     *
     * @param promptId ID du modèle de prompt.
     * @param parameterDTOs Liste des paramètres à enregistrer ou mettre à jour.
     * @return Message de succès ou d'erreur.
     */
    @PostMapping("/{promptId}/parameters")
    public ResponseEntity<String> saveParameters(
            @PathVariable Long promptId,
            @RequestBody List<ParameterDTO> parameterDTOs
    ) {
        try {
            discussionService.saveParameters(parameterDTOs, promptId);
            return ResponseEntity.ok("Paramètres enregistrés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'enregistrement des paramètres : " + e.getMessage());
        }
    }

    /**
     * Mettre à jour un paramètre spécifique pour un modèle de prompt et une interaction donnée.
     *
     * @param promptId ID du modèle de prompt.
     * @param interactionId ID de l'interaction.
     * @param parameterId ID du paramètre à mettre à jour.
     * @param parameterDTO Contient les nouvelles informations du paramètre.
     * @return Le paramètre mis à jour.
     */
    @PutMapping("/{promptId}/interaction/{interactionId}/parameters/{parameterId}")
    public ResponseEntity<Parameters> updateParameter(
            @PathVariable Long promptId,
            @PathVariable Long interactionId,
            @PathVariable Long parameterId,
            @RequestBody ParameterDTO parameterDTO
    ) {
        try {
            Parameters updatedParameter = parametersService.updateParameter(parameterId, parameterDTO);
            return ResponseEntity.ok(updatedParameter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Paramètre non trouvé
        }
    }

    /**
     * Mettre à jour tous les paramètres d'une interaction spécifique.
     *
     * @param promptId ID du modèle de prompt.
     * @param interactionId ID de l'interaction.
     * @param parameterDTOList Liste des nouveaux paramètres.
     * @return Status 200 si les paramètres ont été mis à jour.
     */
    @PutMapping("/{promptId}/interaction/{interactionId}/parameters")
    public ResponseEntity<Void> updateAllParametersForInteraction(
            @PathVariable Long promptId,
            @PathVariable Long interactionId,
            @RequestBody List<ParameterDTO> parameterDTOList
    ) {
        try {
            parametersService.updateParametersForInteraction(promptId, interactionId, parameterDTOList);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Modèle de prompt ou interaction non trouvé
        }
    }

    /**
     * Supprimer un paramètre spécifique d'une interaction donnée.
     *
     * @param promptId ID du modèle de prompt.
     * @param interactionId ID de l'interaction.
     * @param parameterId ID du paramètre à supprimer.
     * @return Status 200 si la suppression a réussi.
     */
    @DeleteMapping("/{promptId}/interaction/{interactionId}/parameters/{parameterId}")
    public ResponseEntity<Void> deleteParameter(
            @PathVariable Long promptId,
            @PathVariable Long interactionId,
            @PathVariable Long parameterId
    ) {
        try {
            parametersService.deleteParameter(parameterId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Paramètre non trouvé
        }
    }

    /**
     * Supprimer plusieurs paramètres d'une interaction donnée.
     *
     * @param promptId ID du modèle de prompt.
     * @param interactionId ID de l'interaction.
     * @param parameterIds Liste des ID des paramètres à supprimer.
     * @return Status 200 si la suppression a réussi.
     */
    @DeleteMapping("/{promptId}/interaction/{interactionId}/parameters")
    public ResponseEntity<Void> deleteMultipleParameters(
            @PathVariable Long promptId,
            @PathVariable Long interactionId,
            @RequestBody List<Long> parameterIds
    ) {
        try {
            parametersService.deleteParameters(parameterIds);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Paramètres non trouvés
        }
    }

    /**
     * Récupérer l'historique de toutes les interactions.
     *
     * @return Liste de toutes les interactions enregistrées.
     */
    @GetMapping("/historique")
    public ResponseEntity<List<HistoriqueInteraction>> getAllHistorique() {
        List<HistoriqueInteraction> historiqueInteractions = discussionService.getHistoriqueInteractions();
        return ResponseEntity.ok(historiqueInteractions);
    }

    /**
     * Récupérer l'historique des interactions pour un modèle de prompt spécifique.
     *
     * @param promptModelId ID du modèle de prompt.
     * @return Liste des interactions associées à ce modèle de prompt.
     */
    @GetMapping("/historique/{promptModelId}")
    public ResponseEntity<List<HistoriqueInteraction>> getHistoriqueByPromptModel(@PathVariable Long promptModelId) {
        List<HistoriqueInteraction> historiqueInteractions = discussionService.getHistoriqueByPromptModel(promptModelId);
        return ResponseEntity.ok(historiqueInteractions);
    }
}
