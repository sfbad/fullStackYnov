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


        desriptionForGOT.append("Plongez dans l'univers épique de Westeros avec 'Game of Throne