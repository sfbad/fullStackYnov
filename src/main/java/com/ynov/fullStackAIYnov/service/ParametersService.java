package com.ynov.fullStackAIYnov.service;

import com.ynov.fullStackAIYnov.model.Parameters;
import com.ynov.fullStackAIYnov.repository.ParametersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ParametersService {

    private final ParametersRepository parametersRepository;

    public List<Parameters> getParametersByPromptModelId(Long promptModelId) {
        return StreamSupport.stream(parametersRepository.findAll().spliterator(), false)
                .filter(param -> param.getModelPrompted().getId() == promptModelId)
                .toList();
    }
}
