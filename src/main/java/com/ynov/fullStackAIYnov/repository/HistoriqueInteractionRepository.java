package com.ynov.fullStackAIYnov.repository;
import com.ynov.fullStackAIYnov.model.HistoriqueInteraction;
import com.ynov.fullStackAIYnov.model.Parameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueInteractionRepository extends JpaRepository<HistoriqueInteraction, Long> {

    List<HistoriqueInteraction> findByModelPrompted_Id(Long promptModelId);

}