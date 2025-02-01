package com.ynov.fullStackAIYnov.service;


import com.ynov.fullStackAIYnov.dto.ScenarioByIDAndTitre;
import com.ynov.fullStackAIYnov.model.Personnage;
import com.ynov.fullStackAIYnov.model.Scenario;
import com.ynov.fullStackAIYnov.repository.ScenarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final PersonnageService personnageService;

    public ScenarioService(ScenarioRepository scenarioRepository, PersonnageService personnageService) {
        this.scenarioRepository = scenarioRepository;
        this.personnageService = personnageService;
    }
    public Scenario save(Scenario scenario) {
        return scenarioRepository.save(scenario);
    }
    public List<Scenario> findAll() {
        return scenarioRepository.findAll();
    }
    public Scenario findById(Long id) {
        return scenarioRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        Scenario scenario = scenarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Scenario not found"));
        List<Personnage> personnages = personnageService.getPersonnagesByScenarioID(id);

        for (Personnage personnage : personnages) {
            personnageService.deletePersonnage(personnage.getId());
        }
        scenarioRepository.deleteById(id);
    }
    public List<Scenario> findByPromptModelId(Long promptModelId) {
        return scenarioRepository.findByModelPrompted_Id(promptModelId);

    }

    /**
     * Récupère l'historique des scénarios pour un modèle de prompt spécifique.
     *
     * @param promptModelId L'ID du modèle de prompt.
     * @return La liste des interactions associées au modèle de prompt.
     */
    public List<Scenario> getScenarioByPromptModel(Long promptModelId) {
        return scenarioRepository.findByModelPrompted_Id(promptModelId);
    }

}


