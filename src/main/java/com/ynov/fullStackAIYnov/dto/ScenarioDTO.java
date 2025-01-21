package com.ynov.fullStackAIYnov.dto;

import lombok.Builder;

import java.util.Locale;

@Builder
public record ScenarioDTO(Long id,String prompt, String tramHistoire, String response) {

}


