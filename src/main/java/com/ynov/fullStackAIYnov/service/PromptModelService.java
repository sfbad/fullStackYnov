package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.model.PromptModel;
import com.ynov.fullStackAIYnov.repository.PromptModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PromptModelService {
    private final FileReaderService fileReaderService;
    private final PromptModelRepository promptModelRepository;

    @Autowired
    public PromptModelService(FileReaderService fileReaderService, PromptModelRepository promptModelRepository) {
        this.fileReaderService = fileReaderService;
        this.promptModelRepository = promptModelRepository;
    }

    public PromptModel loadPromptDescription(Long promptModelId) throws IOException {
        PromptModel PromptModel = promptModelRepository.findById(promptModelId)
                .orElseThrow(() -> new IllegalArgumentException("PromptModel not found with ID: " + promptModelId));
        return PromptModel;
    }
    public boolean createPromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            System.out.println("Un modèle avec ce chemin de fichier existe déjà.");
            return false;
        }
        PromptModel PromptModel = new PromptModel(filePath);
        PromptModel.setDescription(fileReaderService.readInternalFileAsString(filePath));
        promptModelRepository.save(PromptModel);
        return true;
    }
    public PromptModel updatePromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            PromptModel PromptModel = promptModelRepository.getPromptModelsByFilepath(filePath);
            PromptModel.setDescription(fileReaderService.readInternalFileAsString(filePath));
            promptModelRepository.save(PromptModel);
            return PromptModel;
        }
        return null;
    }
    public boolean deletePromptModel(String filePath) throws IOException {
        if (promptModelRepository.existsByFilepath(filePath)) {
            boolean done = promptModelRepository.deletePromptModelByFilepath(filePath);
            return done;
        }
        return false;
    }
    public PromptModel getPromptModelByFilepath(String filePath) {
        if (promptModelRepository.existsByFilepath(filePath)) {
            return promptModelRepository.getPromptModelsByFilepath(filePath);
        }
        return null;
    }
}
