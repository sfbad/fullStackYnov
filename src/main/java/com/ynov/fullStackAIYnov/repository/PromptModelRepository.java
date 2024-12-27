package com.ynov.fullStackAIYnov.repository;

import com.ynov.fullStackAIYnov.model.PromptModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PromptModelRepository extends CrudRepository<PromptModel, Long> {
    Optional<PromptModel> findByDescription(String description);
    boolean existsByFilepath(String filepath);

    PromptModel getPromptModelsByFilepath(String filePath);

    boolean deletePromptModelByFilepath(String filepath);
}


