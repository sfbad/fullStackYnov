package com.ynov.fullStackAIYnov.controller;

import com.ynov.fullStackAIYnov.dto.PersonnageDTO;
import com.ynov.fullStackAIYnov.dto.PromptModelDTO;
import com.ynov.fullStackAIYnov.dto.RequestDTO;
import com.ynov.fullStackAIYnov.dto.ScenarioDTO;
import com.ynov.fullStackAIYnov.model.Scenario;
import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.service.DiscussionService;
import com.ynov.fullStackAIYnov.service.PersonnageService;
import com.ynov.fullStackAIYnov.service.PromptModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatManagementController {

    private final DiscussionService discussionService;
    private final PersonnageService personnageService;
    private final PromptModelService promptModelService;

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
            promptModelService.getPromptModelById(promptId);

            String response = discussionService.processInteraction(promptId, requestDTO);
            return ResponseEntity.ok(response);

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body("Une erreur est survenue : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Créer un nouveau modèle de prompt à partir d'une description.
     *
     * @param promptModelDTO ayant le titre, la decription et l'url de l'image du modèle de prompt.
     * @return la liste de tous les  modèles de prompt créés sous forme de DTO.
     */
    @PostMapping("/prompt/creer")
    public ResponseEntity<List<PromptModelDTO>> createPromptModel(@Valid @RequestBody PromptModelDTO promptModelDTO) {
        try {
            List<PromptModelDTO> promptModelDTOList = new ArrayList<>();
            PromptModel promptModel = promptModelService.createPromptModel(
                    promptModelDTO.titre(), promptModelDTO.description(), promptModelDTO.imgUrl()
            );
            promptModelService.getAllPromptModels().forEach(promptModel1 -> {
                PromptModelDTO promptModelDTO1 = PromptModelDTO.builder()
                                .titre(promptModel1.getTitre())
                                        .description(promptModel1.getDescription())
                                                .imgUrl(promptModel1.getImgUrl())
                        .build();
                promptModelDTOList.add(promptModelDTO1);
                    });
            return ResponseEntity.ok(promptModelDTOList);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
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
        PromptModel promptModel = promptModelService.getPromptModelById(id);
        return ResponseEntity.ok(PromptModelDTO.builder()
                        .titre(promptModel.getTitre())
                        .description(promptModel.getDescription())
                         .imgUrl(promptModel.getImgUrl())
                        .build());
    }

    /**
     * Récupérer les personnages pour un scénario.
     ** @param scenarioID ID du scénario.
     * @return Liste des paramètres associés au modèle de prompt et à l'interaction.
     */
    @GetMapping("/personnages_scenario/{scenarioID}")
    public ResponseEntity<List<Personnage>> getParametersForPromptAndInteraction(
            @PathVariable Long scenarioID
    ) {
        try {
            List<Personnage> parameters = personnageService.getPersonnagesByScenarioID(scenarioID);
            return ResponseEntity.ok(parameters);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

//    /**
//     * Supprimer un paramètre spécifique d'une interaction donnée.
//     *
//     * @param promptId ID du modèle de prompt.
//     * @param interactionId ID de l'interaction.
//     * @param parameterId ID du paramètre à supprimer.
//     * @return Status 200 si la suppression a réussi.
//     */
//    @DeleteMapping("/{promptId}/interaction/{interactionId}/parameters/{parameterId}")
//    public ResponseEntity<Void> deleteParameter(
//            @PathVariable Long promptId,
//            @PathVariable Long interactionId,
//            @PathVariable Long parameterId
//    ) {
//        try {
//            personnageService.deletePersonnage(parameterId);
//            return ResponseEntity.ok().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(404).body(null); // Paramètre non trouvé
//        }
//    }


    /**
     * Récupérer l'historique des scenarios.
     *
     * @return Liste de tous les scénarios enregistrés.
     */
    @GetMapping("/historique")
    public ResponseEntity<List<Scenario>> getAllHistorique() {
        List<Scenario> scenarios = discussionService.getHistoriqueScenario();
        return ResponseEntity.ok(scenarios);
    }

    /**
     * Récupérer l'historique des interactions pour un modèle de prompt spécifique.
     *
     * @param promptModelId ID du modèle de prompt.
     * @return Liste des interactions associées à ce modèle de prompt.
     */
    @GetMapping("/historique/{promptModelId}")
    public ResponseEntity<List<ScenarioDTO>> getHistoriqueByPromptModel(@PathVariable Long promptModelId) {
        List<Scenario> scenarios = discussionService.getScenarioByPromptModel(promptModelId);
        List<ScenarioDTO> scenarioDTOS = new ArrayList<>();
        scenarios.forEach(scenario -> {
            ScenarioDTO scenarioDTO = ScenarioDTO.builder()
                    .prompt(scenario.getPrompt())
                    .tramHistoire(scenario.getTramHistoire())
                    .response(scenario.getResponse())
                    .build();
            scenarioDTOS.add(scenarioDTO);
        });
        return ResponseEntity.ok(scenarioDTOS);
    }
}
