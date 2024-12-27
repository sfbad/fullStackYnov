package com.ynov.fullStackAIYnov.config;


import com.ynov.fullStackAIYnov.service.PromptModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptsModelsLoader {
    private static final String PATHFORGOT1 = "prompts/Testprompt.txt";
//    private static final String PATHFORGOT2 = "prompts/Testprompt.txt";
//    private static final String PATHFORGOT3 = "prompts/Testprompt.txt";
//    private static final String PATHFORGOT4 = "prompts/Testprompt.txt";
//    private static final String PATHFORGOT5 = "prompts/Testprompt.txt";
//    private static final String PATHFORGOT6=  "prompts/Testprompt.txt";

  CommandLineRunner loadPromptsModels(PromptModelService promptModelService) {
      return args -> {
          promptModelService.createPromptModel(PATHFORGOT1);
      };
  }





}
