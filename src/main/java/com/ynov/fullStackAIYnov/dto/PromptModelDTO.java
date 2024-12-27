package com.ynov.fullStackAIYnov.dto;

import lombok.Builder;

import java.sql.Clob;

@Builder
public record PromptModelDTO(String description, String filepath) {
}
