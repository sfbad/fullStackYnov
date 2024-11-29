package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.ChatManagement;
import com.ynov.fullStackAIYnov.repository.ChatManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final OllamaChatModel ollamaChatModel;
    private final ChatManagementRepository chatManagementRepository;
    private final PromptModelService promptModelService;

    public String processInteraction(Long promptModelId, String userQuestion) throws SQLException, IOException {

        if (promptModelId == null || userQuestion == null || userQuestion.isEmpty()) {
            throw new IllegalArgumentException("PromptModel ID ou question utilisateur invalide.");
        }

        String promptDescription = promptModelService.loadPromptDescription(promptModelId).getDescription();

        if (promptDescription == null || promptDescription.isEmpty()) {
            throw new IllegalArgumentException("La description du prompt est vide ou non trouvée.");
        }

        String iaResponse = sendToAI(promptDescription, userQuestion);

        ChatManagement chatManagement = new ChatManagement();
        chatManagement.setPrompt(promptDescription);
        chatManagement.setQuestion(userQuestion);
        chatManagement.setResponse(iaResponse);
        chatManagementRepository.save(chatManagement);

        return iaResponse;
    }

    private String sendToAI(String promptDescription, String userQuestion) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(promptDescription));
            System.out.println("la question "+userQuestion);
            messages.add(new UserMessage(userQuestion));

            Prompt promptToSend = new Prompt(messages);
            Flux<ChatResponse> chatResponses = ollamaChatModel.stream(promptToSend);

            return Optional.ofNullable(chatResponses.collectList().block())
                    .orElseThrow(() -> new IOException("Aucune réponse reçue de l'IA."))
                    .stream()
                    .map(response -> response.getResult().getOutput().getContent())
                    .collect(Collectors.joining(""));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'interaction avec l'IA : " + e.getMessage(), e);
        }
    }
}
