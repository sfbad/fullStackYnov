package com.ynov.fullStackAIYnov.config;

import com.ynov.fullStackAIYnov.service.PromptModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PromptsModelsConfig {

    private final String promptTitreEtImgUrlPourGOT = "Game Of Thrones";
    private final String promptTitreEtImgUrlPourVK = "Viking";
    private final String promptTitreEtImgUrlPourCP = "Casa de papel";

    private final StringBuilder descriptionForGOT = new StringBuilder();
    private final StringBuilder descriptionForVikings = new StringBuilder();
    private final StringBuilder descriptionForCasaDePapel = new StringBuilder();

    // Constructeur pour injection des dépendances
    public PromptsModelsConfig() {
        descriptionForGOT.append("Plongez dans l'univers épique de Westeros avec 'Game of Thrones'.")
                .append("Une série pleine d'aventures, de trahisons et de dragons. Parfaite pour les amateurs de fantasy.")
                .append(System.lineSeparator());

        descriptionForVikings.append("Revivez les exploits légendaires de Ragnar Lothbrok et ses fils dans 'Vikings'.")
                .append("Une série historique captivante qui explore la culture et les batailles des Vikings.")
                .append(System.lineSeparator());

        descriptionForCasaDePapel.append("La Casa de Papel' raconte l'histoire audacieuse du Professeur et de son équipe ")
                .append("alors qu'ils réalisent les plus grands braquages de l'histoire. Une série haletante pleine de suspense.")
                .append(System.lineSeparator());
    }

    @Bean
    public CommandLineRunner loadPromptsModels(PromptModelService promptModelService) {
        return args -> {

            promptModelService.createPromptModel(promptTitreEtImgUrlPourGOT,
                    descriptionForGOT.toString(), "go");

            promptModelService.createPromptModel(promptTitreEtImgUrlPourVK,
                    descriptionForVikings.toString(), "go");

            promptModelService.createPromptModel(promptTitreEtImgUrlPourCP,
                    descriptionForCasaDePapel.toString(), "go");
        };
    }
}
