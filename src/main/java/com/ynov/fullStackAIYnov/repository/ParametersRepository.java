package com.ynov.fullStackAIYnov.repository;

import com.ynov.fullStackAIYnov.model.Parameters;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ParametersRepository extends CrudRepository<Parameters, Long> {
    List<Parameters> findByModelPromptedId(Long promptModelId);

    Optional<Parameters> findByNomPersonnage(String nomPersonnage);

    List<Parameters> findAllByModelPrompted_IdAndHistoriqueInteraction_Id(Long promptModelId, Long interactionId);

    List<Parameters> findAllByHistoriqueInteraction_Id(Long interactionId);

    //ic je

}
