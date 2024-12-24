package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.dto.ParameterDTO;
import com.ynov.fullStackAIYnov.dto.RequestDTO;
import com.ynov.fullStackAIYnov.model.HistoriqueInteraction;
import com.ynov.fullStackAIYnov.model.Parameters;
import com.ynov.fullStackAIYnov.repository.HistoriqueInteractionRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des interactions de discussion avec l'IA (Ollama).
 * Ce service gère les requêtes utilisateur, l'envoi de ces requêtes à l'IA,
 * l'enregistrement des historiques d'interaction, et la gestion des paramètres associés.
 *
 * <p>Méthodes :
 * 1. processInteraction(Long promptModelId, RequestDTO requestDTO) - Traite une interaction utilisateur avec l'IA.
 * 2. sendToAI(String promptDescription, StringBuilder userQuestion) - Envoie la question de l'utilisateur à l'IA et récupère la réponse.
 * 3. saveParameters(List<ParameterDTO> parameterDTOS, Long promptModelId) - Sauvegarde ou met à jour les paramètres associés à un modèle de prompt.
 * 4. getHistoriqueInteractions() - Récupère l'historique de toutes les interactions.
 * 5. getHistoriqueByPromptModel(Long promptModelId) - Récupère l'historique des interactions pour un modèle de prompt spécifique.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final OllamaChatModel ollamaChatModel;
    private final PromptModelService promptModelService;
    private final HistoriqueInteractionRepository historiqueInteractionRepository;
    private final ParametersService parametersService;

    /**
     * Traite une interaction utilisateur avec l'IA.
     * Envoie la requête à l'IA, enregistre l'historique de l'interaction et renvoie la réponse de l'IA.
     *
     * @param promptModelId L'ID du modèle de prompt à utiliser pour générer la réponse.
     * @param requestDTO L'objet contenant la question utilisateur et les paramètres associés.
     * @return La réponse de l'IA sous forme de chaîne de caractères.
     * @throws SQLException Si une erreur se produit avec la base de données.
     * @throws IOException Si une erreur se produit lors de l'envoi de la requête à l'IA.
     */
    public String processInteraction(Long promptModelId, RequestDTO requestDTO) throws SQLException, IOException {
        StringBuilder requestScenario = requestDTO.getTramHistoireWithDetails();

        if (promptModelId == null || requestScenario.isEmpty()) {
            throw new IllegalArgumentException("PromptModel ID ou question utilisateur invalide.");
        }

        String promptDescription = promptModelService.loadPromptDescription(promptModelId).getDescription();

        if (promptDescription == null || promptDescription.isEmpty()) {
            throw new IllegalArgumentException("La description du prompt est vide ou non trouvée.");
        }
        saveParameters(requestDTO.parameterDTOS(), promptModelId);

        String iaResponse = sendToAI(promptDescription, requestScenario);
        System.out.println(iaResponse);

        // Enregistrement de l'historique de l'interaction
        HistoriqueInteraction historiqueInteraction = new HistoriqueInteraction();
        historiqueInteraction.setPrompt(promptDescription);
        historiqueInteraction.setQuestion(requestScenario);
        historiqueInteraction.setResponse(iaResponse);
        historiqueInteractionRepository.save(historiqueInteraction);

        return iaResponse;
    }

    /**
     * Envoie une question utilisateur à l'IA et récupère la réponse.
     *
     * @param promptDescription La description du prompt à envoyer à l'IA.
     * @param userQuestion La question de l'utilisateur à envoyer.
     * @return La réponse de l'IA sous forme de chaîne de caractères.
     * @throws IOException Si une erreur se produit lors de l'envoi de la requête à l'IA.
     */
    private String sendToAI(String promptDescription, StringBuilder userQuestion) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(promptDescription));  // Ajout du prompt système
            System.out.println("la question " + userQuestion);
            messages.add(new UserMessage(String.valueOf(userQuestion)));  // Ajout du message utilisateur

            Prompt promptToSend = new Prompt(messages);  // Création de la requête
            Flux<ChatResponse> chatResponses = ollamaChatModel.stream(promptToSend);

            // Collecte des réponses de l'IA
            return Optional.ofNullable(chatResponses.collectList().block())
                    .orElseThrow(() -> new IOException("Aucune réponse reçue de la part de Ollama."))
                    .stream()
                    .map(response -> response.getResult().getOutput().getContent())  // Extraction du contenu de la réponse
                    .collect(Collectors.joining(""));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'interaction avec Ollama : " + e.getMessage(), e);
        }
    }

    /**
     * Sauvegarde ou met à jour les paramètres associés à un modèle de prompt.
     * Si le paramètre existe déjà, il est mis à jour ; sinon, un nouveau paramètre est créé.
     *
     * @param parameterDTOS La liste des paramètres à sauvegarder ou mettre à jour.
     * @param promptModelId L'ID du modèle de prompt auquel les paramètres sont associés.
     * @return `true` si l'opération est réussie.
     */
    public boolean saveParameters(List<ParameterDTO> parameterDTOS, Long promptModelId) {
        parameterDTOS.forEach(parameterDTO -> {
            Parameters existingParam = parametersService.findByNomPersonnage(parameterDTO.nomPersonnage());

            if (existingParam != null && Objects.equals(existingParam.getModelPrompted().getId(), promptModelId)) {
                existingParam.setTraitsPersonnalite(parameterDTO.traitsPersonnalite());
                existingParam.setDescription(parameterDTO.description());
                parametersService.save(existingParam);
            } else {
                Parameters param = new Parameters();
                param.setNomPersonnage(parameterDTO.nomPersonnage());
                param.setTraitsPersonnalite(parameterDTO.traitsPersonnalite());
                param.setDescription(parameterDTO.description());

                try {
                    param.setModelPrompted(promptModelService.loadPromptDescription(promptModelId));  // Associe le modèle de prompt
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                parametersService.save(param);
            }
        });
        return true;
    }

    /**
     * Récupère l'historique de toutes les interactions.
     *
     * @return La liste de toutes les interactions enregistrées dans l'historique.
     */
    public List<HistoriqueInteraction> getHistoriqueInteractions() {
        return historiqueInteractionRepository.findAll();
    }

    /**
     * Récupère l'historique des interactions pour un modèle de prompt spécifique.
     *
     * @param promptModelId L'ID du modèle de prompt.
     * @return La liste des interactions associées au modèle de prompt.
     */
    public List<HistoriqueInteraction> getHistoriqueByPromptModel(Long promptModelId) {
        return historiqueInteractionRepository.findByModelPrompted_Id(promptModelId);
    }
}
