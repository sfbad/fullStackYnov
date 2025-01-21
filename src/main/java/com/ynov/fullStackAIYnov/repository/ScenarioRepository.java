package com.ynov.fullStackAIYnov.repository;
import com.ynov.fullStackAIYnov.dto.ScenarioByIDAndTitre;
import com.ynov.fullStackAIYnov.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    List<Scenario> findByModelPrompted_Id(Long promptModelId);
    @Query("SELECT s.id as id, s.titre AS titre FROM Scenario s")
    List<ScenarioByIDAndTitre> findByIdAndTitre();


}