package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String tramHistoire;

    @Column(columnDefinition = "TEXT")
    private String response;
    @ManyToOne
    @JoinColumn(name = "prompt_model_id")
    private PromptModel modelPrompted;
    
}
