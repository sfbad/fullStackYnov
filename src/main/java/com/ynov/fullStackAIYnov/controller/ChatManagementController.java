package com.ynov.fullStackAIYnov.controller;

import com.ynov.fullStackAIYnov.dto.PromptModelDTO;
import com.ynov.fullStackAIYnov.dto.QuestionDTO;
import com.ynov.fullStackAIYnov.model.ChatManagement;
import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.repository.PromptModelRepository;
import com.ynov.fullStackAIYnov.repository.ChatManagementRepository;
import com.ynov.fullStackAIYnov.service.DiscussionService;
import com.ynov.fullStackAIYnov.service.PromptModelService;
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
    private final ChatManagementRepository chatManagementRepository;
    private final PromptModelRepository promptModelRepository;
    private final PromptModelService promptModelService;


    @PostMapping("/create")
    public ResponseEntity<PromptModel> createPrompt(@RequestParam String promptModelFilePath) throws IOException {
        boolean isCreated = promptModelService.createPromptModel(promptModelFilePath);
        if (isCreated) {
            return ResponseEntity.ok(promptModelService.getPromptModelByFilepath(promptModelFilePath));
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{promptId}/ask")
    public ResponseEntity<String> askQuestion(@PathVariable Long promptId, @RequestBody QuestionDTO questionDTO) {
        try {
            System.out.println(questionDTO.question());
            PromptModel PromptModel = promptModelRepository.findById(promptId)
                    .orElseThrow(() -> new IllegalArgumentException("PromptModel not found with ID: " + promptId));

            String response = discussionService.processInteraction(promptId, questionDTO.question());

            ChatManagement chatManagement = new ChatManagement();
            chatManagement.setPrompt(PromptModel.getDescription());
            chatManagement.setQuestion(questionDTO.question());
            chatManagement.setResponse(response);
            chatManagementRepository.save(chatManagement);

            return ResponseEntity.ok(response);

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint pour récupérer l'historique des conversations avec l'IA.
     * @return Une liste des échanges enregistrés.
     */
    @GetMapping("/history")
    public ResponseEntity<List<ChatManagement>> getChatHistory() {
        List<ChatManagement> chatHistory = (List<ChatManagement>) chatManagementRepository.findAll();
        return ResponseEntity.ok(chatHistory);
    }

    /**
     * Endpoint pour obtenir un modèle de prompt par son ID.
     * @param id L'ID du modèle de prompt.
     * @return Le modèle PromptModel.
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
}
