package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.dto.PersonnageDTO;
import com.ynov.fullStackAIYnov.dto.RequestDTO;
import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.model.Scenario;
import com.ynov.fullStackAIYnov.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Service pour la gestion des interactions de discussion avec l'IA (Ollama).
 * Ce service gère les requêtes utilisateur, l'envoi de ces requêtes à l'IA,
 * l'enregistrement des historiques d'interaction, et la gestion des paramètres associés.
 *
 * <p>Méthodes :
 * 1. processInteraction(Long promptModelId, RequestDTO requestDTO) - Traite une interaction utilisateur avec l'IA.
 * 2. sendToAI(String promptDescription, StringBuilder userQuestion) - Envoie la question de l'utilisateur à l'IA et récupère la réponse.
 * 3. saveParameters(List<PersonnageDTO> personnageDTOS, Long promptModelId) - Sauvegarde ou met à jour les paramètres associés à un modèle de prompt.
 * 4. getHistoriqueInteractions() - Récupère l'historique de toutes les interactions.
 * 5. getHistoriqueByPromptModel(Long promptModelId) - Récupère l'historique des interactions pour un modèle de prompt spécifique.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final OllamaChatModel ollamaChatModel;
    private final PromptModelService promptModelService;
    private final ScenarioRepository scenarioRepository;
    private final PersonnageService personnageService;

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
            throw new IllegalArgumentException("Prompt ID ou question utilisateur invalide.");
        }

        String promptDescription = promptModelService.getPromptModelById(promptModelId).getDescription();

        if (promptDescription == null || promptDescription.isEmpty()) {
            throw new IllegalArgumentException("La description du prompt est vide ou non trouvée.");
        }

        log.info("Envoie de la tram à OLLAMA debut ");

        String iaResponse = sendToAI(promptDescription, requestScenario);
        log.info("Début persistance du scenario : ");

        Scenario scenario = new Scenario();
        scenario.setPrompt(promptDescription);
        scenario.setTramHistoire(requestDTO.tramHistoire());
        scenario.setResponse(iaResponse);
        log.info("Fin persistance du scenario : " + scenario);

        scenarioRepository.save(scenario);
        List<PersonnageDTO> personnageDTOS = requestDTO.personnageDTOS();
        log.info("Envoie de la tram à OLLAMA fin  ");


        log.info("debut persistance des personnages : " + personnageDTOS);
        personnageDTOS.forEach(personnageDTO -> {
            Personnage personnage = Personnage.builder()
                    .nomPersonnage(personnageDTO.nomPersonnage())
                    .traitsPersonnalite(personnageDTO.traitsPersonnalite())
                    .description(personnageDTO.description())
                    .build();
            personnage.setScenario(scenario);
            personnageService.save(personnage);
        });
        log.info("Persistance des personnages fin ");


        return iaResponse;
    }

    /**
     * Envoie une question utilisateur à l'IA et récupère la réponse.
     *
     * @param promptDescription La description du prompt à envoyer à l'IA.
     * @param userQuestion La question ou le tram + les personnages de l'utilisateur à envoyer.
     * @return La réponse de l'IA sous forme de chaîne de caractères.
     * @throws IOException Si une erreur se produit lors de l'envoi de la requête à l'IA.
     */
    private String sendToAI(String promptDescription, StringBuilder userQuestion) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(promptDescription));
            messages.add(new UserMessage(String.valueOf(userQuestion)));

            Prompt promptToSend = new Prompt(messages);
            Flux<ChatResponse> chatResponses = ollamaChatModel.stream(promptToSend);

            return Optional.ofNullable(chatResponses.collectList().block())
                    .orElseThrow(() -> new IOException("Aucune réponse reçue de la part de Ollama."))
                    .stream()
                    .map(response -> response.getResult().getOutput().getContent())
                    .collect(Collectors.joining(""));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'interaction avec Ollama : " + e.getMessage(), e);
        }
    }


}
