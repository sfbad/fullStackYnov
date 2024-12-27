package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Parameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomPersonnage;


    @ElementCollection
    private List<String> traitsPersonnalite;

    private String description;

    @ManyToOne
    @JoinColumn(name = "prompt_model_id")
    private PromptModel modelPrompted;


    @ManyToOne
    @JoinColumn(name = "historique_interaction_id")
    private HistoriqueInteraction historiqueInteraction;

}
