package com.ynov.fullStackAIYnov.repository;

import com.ynov.fullStackAIYnov.model.Personnage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonnageRepository extends CrudRepository<Personnage, Long> {
    List<Personnage> findByScenario_Id(Long scenarioID);
}
