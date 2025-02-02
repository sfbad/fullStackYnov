package com.ynov.fullStackAIYnov.service;
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



}


