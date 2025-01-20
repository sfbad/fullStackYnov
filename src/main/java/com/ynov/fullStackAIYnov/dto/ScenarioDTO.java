package com.ynov.fullStackAIYnov.dto;

import lombok.Builder;

import java.util.Locale;

@Builder
public record ScenarioDTO(String prompt, String tramHistoire, String response) {

}


