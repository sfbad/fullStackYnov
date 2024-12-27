package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.repository.PromptModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Classe de service pour la gestion des opérations liées aux modèles de prompt.
 * Ce service fournit des méthodes pour créer, mettre à jour, supprimer et charger
 * des objets PromptModel. Il interagit avec le PromptModelRepository et le FileReaderService
 * pour effectuer des opérations basées sur des fichiers et stocker ou récupérer des données
 * de modèles de prompt depuis la base de données.
 *
 * <p>Méthodes :
 * 1. loadPromptDescription(Long promptModelId) - Charge la description d'un PromptModel par son ID.
 * 2. createPromptModel(String filePath) - Crée un nouveau PromptModel à partir d'un fichier situé à l'emplacement spécifié.
 * 3. updatePromptModel(String filePath) - Met à jour la description d'un PromptModel existant avec un nouveau fichier.
 * 4. deletePromptModel(String filePath) - Supprime un PromptModel en fonction de son chemin de fichier.
 * 5. getPromptModelByFilepath(String filePath) - Récupère un PromptModel en fonction de son chemin de fichier.
 * 6. loadPromptModelById(Long promptModelId) - Charge un PromptModel en fonction de son ID.
 * </p>
 */
@Service
public class PromptModelService {

    private final FileReaderService fileReaderService;
    private final PromptModelRepository promptModelRepository;

    @Autowired
    public PromptModelService(FileReaderService fileReaderService, PromptModelRepository promptModelRepository) {
        this.fileReaderService = fileReaderService;
        this.promptModelRepository = promptModelRepository;
    }

    /**
     * Charge la description d'un PromptModel en fonction de son ID.
     *
     * @param promptModelId L'ID du modèle de prompt à récupérer.
     * @return Le modèle de prompt correspondant à l'ID donné.
     * @throws IOException Si une erreur survient lors de la lecture du fichier.
     * @throws IllegalArgumentException Si aucun modèle de prompt n'est trouvé avec l'ID donné.
     */
    public PromptModel loadPromptDescription(Long promptModelId) throws IOException {
        PromptModel promptModel = promptModelRepository.findById(promptModelId)
                .orElseThrow(() -> new IllegalArgumentException("PromptModel introuvable avec l'ID : " + promptModelId));
        return promptModel;
    }

    /**
     * Crée un nouveau modèle de prompt à partir d'un fichier situé à l'emplacement spécifié.
     *
     * @param filePath Le chemin vers le fichier contenant la description du modèle de prompt.
     * @return `true` si le modèle a été créé avec succès, `false` si un modèle avec le même chemin de fichier existe déjà.
     * @throws IOException Si une erreur survient lors de la lecture du fichier ou de la sauvegarde du modèle.
     */
    public boolean createPromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            System.out.println("Un modèle avec ce chemin de fichier existe déjà.");
            return false;
        }
        PromptModel promptModel = new PromptModel(filePath);
        promptModel.setDescription(fileReaderService.readInternalFileAsString(filePath));
        promptModelRepository.save(promptModel);
        return true;
    }

    /**
     * Met à jour un modèle de prompt existant en mettant à jour sa description à partir d'un fichier.
     *
     * @param filePath Le chemin vers le fichier contenant la nouvelle description.
     * @return Le modèle de prompt mis à jour, ou `null` si aucun modèle n'a été trouvé avec le chemin de fichier spécifié.
     * @throws IOException Si une erreur survient lors de la lecture du fichier ou de la sauvegarde du modèle.
     */
    public PromptModel updatePromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            PromptModel promptModel = promptModelRepository.getPromptModelsByFilepath(filePath);
            promptModel.setDescription(fileReaderService.readInternalFileAsString(filePath));
            promptModelRepository.save(promptModel);
            return promptModel;
        }
        return null;
    }

    /**
     * Supprime un modèle de prompt en fonction de son chemin de fichier.
     *
     * @param filePath Le chemin du fichier du modèle de prompt à supprimer.
     * @return `true` si le modèle a été supprimé avec succès, `false` si aucun modèle n'a été trouvé avec ce chemin de fichier.
     * @throws IOException Si une erreur survient lors de la suppression du modèle.
     */
    public boolean deletePromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            boolean done = promptModelRepository.deletePromptModelByFilepath(filePath);
            return done;
        }
        return false;
    }

    /**
     * Récupère un modèle de prompt en fonction de son chemin de fichier.
     *
     * @param filePath Le chemin du fichier du modèle de prompt à récupérer.
     * @return Le modèle de prompt associé au chemin de fichier donné, ou `null` si aucun modèle n'est trouvé.
     */
    public PromptModel getPromptModelByFilepath(String filePath) {
        if (promptModelRepository.existsByFilepath(filePath)) {
            return promptModelRepository.getPromptModelsByFilepath(filePath);
        }
        return null;
    }

    /**
     * Charge un modèle de prompt en fonction de son ID.
     *
     * @param promptModelId L'ID du modèle de prompt à charger.
     * @return Le modèle de prompt avec l'ID donné.
     * @throws IOException Si une erreur survient lors de la récupération du modèle.
     * @throws IllegalArgumentException Si aucun modèle de prompt n'est trouvé avec l'ID donné.
     */
    public PromptModel loadPromptModelById(Long promptModelId) throws IOException {
        return promptModelRepository.findById(promptModelId).orElseThrow(
                () -> new IllegalArgumentException("PromptModel introuvable avec l'ID : " + promptModelId)
        );
    }
}
