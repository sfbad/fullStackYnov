package com.ynov.fullStackAIYnov.config;


import com.ynov.fullStackAIYnov.service.PromptModelService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PromptsModelsConfig {
    Pair<String,String> promptTitreEtImgUrlPourGOT = new Pair<>("Game Of Thrones","go");
    Pair<String,String> promptTitreEtImgUrlPourVK = new Pair<>("Viking","go");
    Pair<String,String> promptTitreEtImgUrlPourCP = new Pair<>("Casa de papel","go");

    private final StringBuilder desriptionForGOT = new StringBuilder();
    private final StringBuilder desriptionForVikings = new StringBuilder();
    private final StringBuilder desriptionForCasaDePapel = new StringBuilder();

    CommandLineRunner loadPromptsModels(PromptModelService promptModelservice) {


        desriptionForGOT.append("Plongez dans l'univers épique de Westeros avec 'Game of Thrones'.")
                .append("Une série pleine d'aventures, de trahisons et de dragons. Parfaite pour les amateurs de fantasy.")
                .append(System.lineSeparator());

        desriptionForVikings.append("Revivez les exploits légendaires de Ragnar Lothbrok et ses fils dans 'Vikings'.")
                .append("Une série historique captivante qui explore la culture et les batailles des Vikings.")
                .append(System.lineSeparator());

        desriptionForCasaDePapel.append("La Casa de Papel' raconte l'histoire audacieuse du Professeur et de son équipe ")
                .append("alors qu'ils réalisent les plus grands braquages de l'histoire. Une série haletante pleine de suspense.")
                .append(System.lineSeparator());

        return args -> {

          promptModelservice.createPromptModel(promptTitreEtImgUrlPourGOT.a,
                  desriptionForGOT.toString(),promptTitreEtImgUrlPourGOT.b);

          promptModelservice.createPromptModel(promptTitreEtImgUrlPourVK.a,
                  desriptionForVikings.toString(),
                  promptTitreEtImgUrlPourVK.b);

          promptModelservice.createPromptModel(promptTitreEtImgUrlPourCP.a,
                  desriptionForCasaDePapel.toString(),
                  promptTitreEtImgUrlPourCP.b);
      };
  }


}
