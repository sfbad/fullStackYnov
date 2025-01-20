package com.ynov.fullStackAIYnov.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.sql.Clob;

@Builder
public record PromptModelDTO(
        @NotNull(message = "Veuillez spécifier un titre.")
        String titre,
        @NotNull(message = "la description du prompt est obligatoire.")
        String description,
        @NotNull
        String imgUrl) {


}

