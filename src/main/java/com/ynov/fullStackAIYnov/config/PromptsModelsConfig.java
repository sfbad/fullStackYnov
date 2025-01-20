package com.ynov.fullStackAIYnov.config;


import com.ynov.fullStackAIYnov.service.PromptModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptsModelsConfig {
    private final StringBuilder desriptionForGOT = new StringBuilder();
    private final StringBuilder desriptionForVikings = new StringBuilder();

    private final StringBuilder desriptionForCasaDePapel = new StringBuilder();

    CommandLineRunner loadPromptsModels(PromptModelService configFromFilService) {

        desriptionForGOT.append("zafrgetgtgrhfyhyyntnntntgjnykk;kj;u;olololoo")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        desriptionForVikings.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        desriptionForCasaDePapel.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator());

      return args -> {
          configFromFilService.createPromptModel(desriptionForGOT.toString());
          configFromFilService.createPromptModel(desriptionForVikings.toString());
          configFromFilService.createPromptModel(desriptionForCasaDePapel.toString());
      };
  }


}
