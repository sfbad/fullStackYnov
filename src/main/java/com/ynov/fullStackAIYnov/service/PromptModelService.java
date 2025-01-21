package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.repository.PromptModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des opérations liées à la classe PromptModel.
 *
 * Ce service fournit des méthodes pour créer, mettre à jour, supprimer et récupérer
 * des objets PromptModel depuis la base de données.
 */
@Service
public class PromptModelService {

    private final PromptModelRepository modelPromptRepository;

    @Autowired
    public PromptModelService(PromptModelRepository modelPromptRepository) {
        this.modelPromptRepository = modelPromptRepository;
    }

    /**
     * Récupère un PromptModel par son ID.
     *
     * @param id L'ID du PromptModel à récupérer.
     * @return Le PromptModel correspondant, ou une exception si non trouvé.
     * @throws IllegalArgumentException Si aucun PromptModel n'est trouvé avec l'ID donné.
     */
    public PromptModel getPromptModelById(Long id) {
        return modelPromptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PromptModel introuvable avec l'ID : " + id));
    }

    /**
     * Crée un nouveau PromptModel.
     *
     * @param description La description du nouveau PromptModel.
     * @return Le PromptModel créé.
     */
    public PromptModel createPromptModel(String titre,String description, String imgUrl) {
        PromptModel promptModel = new PromptModel();
        promptModel.setTitre(titre);
        promptModel.setDescription(description);
        promptModel.setImgUrl(imgUrl);
        return modelPromptRepository.save(promptModel);
    }

    /**
     * Met à jour un PromptModel existant.
     *
     * @param id          L'ID du PromptModel à mettre à jour.
     * @param description La nouvelle description à appliquer.
     * @return Le PromptModel mis à jour.
     * @throws IllegalArgumentException Si aucun PromptModel n'est trouvé avec l'ID donné.
     */
    public PromptModel updatePromptModel(Long id, String description) {
        PromptModel promptModel = getPromptModelById(id);
        promptModel.setDescription(description);
        return modelPromptRepository.save(promptModel);
    }

    /**
     * Supprime un PromptModel par son ID.
     *
     * @param id L'ID du PromptModel à supprimer.
     * @throws IllegalArgumentException Si aucun PromptModel n'est trouvé avec l'ID donné.
     */
    public void deletePromptModel(Long id) {
        if (!modelPromptRepository.existsById(id)) {
            throw new IllegalArgumentException("PromptModel introuvable avec l'ID : " + id);
        }
        modelPromptRepository.deleteById(id);
    }


    /**
     * Récupère tous les PromptModels.
     *
     * @return Une liste de tous les PromptModels.
     */
    public Iterable<PromptModel> getAllPromptModels() {
        return modelPromptRepository.findAll();
    }

    /**
     * Verifie si un prompt model existe
     *
     */
    public  boolean existsPromptModelById(Long id) {
        return modelPromptRepository.existsById(id);
    }

}


