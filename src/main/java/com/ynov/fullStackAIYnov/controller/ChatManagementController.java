package com.ynov.fullStackAIYnov.controller;

import com.ynov.fullStackAIYnov.dto.*;
import com.ynov.fullStackAIYnov.model.Scenario;
import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.service.DiscussionService;
import com.ynov.fullStackAIYnov.service.PersonnageService;
import com.ynov.fullStackAIYnov.service.PromptModelService;
import com.ynov.fullStackAIYnov.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatManagementController {

    private final DiscussionService discussionService;
    private final PersonnageService personnageService;
    private final PromptModelService promptModelService;
    private final ScenarioService scenarioService;


    @PostMapping("/{promptId}/scenariogenerate")
    public ResponseEntity<String> request(@RequestBody Long promptmodel_Id, @RequestBody RequestDTO requestDTO) {
        try {
            promptModelService.getPromptModelById(promptmodel_Id);

            String response = discussionService.processInteraction(promptmodel_Id, requestDTO);
            return ResponseEntity.ok(response);

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body("Une erreur est survenue : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/promptcreate")
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
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromptModel(@PathVariable Long id, @RequestBody PromptModelDTO promptModelDTO) {
        if(!promptModelService.existsPromptModelById(id)){
            return ResponseEntity.status(404).body("Prompt model not found");
        }
        PromptModel promptModel = promptModelService.getPromptModelById(id);
        promptModel.setTitre(promptModelDTO.titre());
        promptModel.setDescription(promptModelDTO.description());
        promptModel.setImgUrl(promptModelDTO.imgUrl());
        promptModelService.savePromptModel(promptModel);
        return ResponseEntity.ok("Prompt model updated !!!");

    }
    @GetMapping("/model/{id}")
    public ResponseEntity<PromptModelDTO> getPromptById(@PathVariable Long id) {
        PromptModel promptModel = promptModelService.getPromptModelById(id);
        return ResponseEntity.ok(PromptModelDTO.builder()
                .titre(promptModel.getTitre())
                .description(promptModel.getDescription())
                .imgUrl(promptModel.getImgUrl())
                .build());
    }
    @GetMapping("/promptmodels")
    public ResponseEntity<List<PromptModelDTO>> getAllPromptModels() {
        List<PromptModelDTO> promptModelDTOList = new ArrayList<>();
        promptModelService.getAllPromptModels().forEach(promptModel -> {
            PromptModelDTO personnageDTO =PromptModelDTO.builder()
                    .id(promptModel.getId())
                    .titre(promptModel.getTitre())
                    .description(promptModel.getDescription())
                    .imgUrl(promptModel.getImgUrl())
                    .build();

            promptModelDTOList.add(personnageDTO);
        });
        return ResponseEntity.ok(promptModelDTOList);
    }
    @GetMapping("/historique/{promptModelId}")
    public ResponseEntity<List<ScenarioDTO>> getHistoriqueByPromptModel(@PathVariable Long promptModelId) {
        List<Scenario> scenarios = scenarioService.getScenarioByPromptModel(promptModelId);
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
    @GetMapping("/scenarioList")
    public List<ScenarioByIDAndTitre> getScenarioByTitreAndTramHistoire(){
        return scenarioService.findByIdAndTitre();
    }

    @DeleteMapping("/delete/scenario")
    public ResponseEntity<String> deleteScenario(@RequestParam Long scenarioID) {
        scenarioService.deleteById(scenarioID);
        return ResponseEntity.ok("Scenario deleted");
    }
    @GetMapping("/personnages")
    public ResponseEntity<List<PersonnageDTO>> getPersonnages(@RequestParam Long scenarioID) {
        List<PersonnageDTO> personnageDTOS = getPersonnageDTOS(scenarioID);
        return ResponseEntity.ok(personnageDTOS);
    }
    @DeleteMapping("/delete_personnage")
    public ResponseEntity<String> deletePersonnage(@RequestParam Long personnageID) {
        personnageService.deletePersonnage(personnageID);
        return ResponseEntity.ok("Personnage deleted");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePromptModel(@RequestBody Long id) {
        promptModelService.deletePromptModel(id);
        return ResponseEntity.ok("Prompt model deleted");
    }

    @GetMapping("/scenarioDetail/{scenarioID}")
    public ResponseEntity< Pair<ScenarioDTO,List<PersonnageDTO> >> getScenarioById(@PathVariable Long scenarioID) {
        Scenario scenario = scenarioService.findById(scenarioID);
        ScenarioDTO scenarioDTO = ScenarioDTO.builder()

                .id(scenario.getId())
                .titre(scenario.getTitre())
                .prompt(scenario.getPrompt())
                .tramHistoire(scenario.getTramHistoire())
                .response(scenario.getResponse())
                .build();

        List<PersonnageDTO> personnageDTOS = getPersonnageDTOS(scenarioID);
        return ResponseEntity.ok(new Pair<>(scenarioDTO, personnageDTOS));
    }

    //Methode utilitaire
    private List<PersonnageDTO> getPersonnageDTOS(Long scenarioID) {
        List<Personnage> personnages = personnageService.getPersonnagesByScenarioID(scenarioID);
        List<PersonnageDTO> personnageDTOS = new ArrayList<>();
        personnages.forEach(personnage -> {
            PersonnageDTO personnageDTO = PersonnageDTO.builder()

                    .id(personnage.getId())
                    .nomPersonnage(personnage.getNomPersonnage())
                    .traitsPersonnalite(personnage.getTraitsPersonnalite())
                    .description(personnage.getDescription())
                    .build();
            personnageDTOS.add(personnageDTO);
        });
        return personnageDTOS;
    }

}
