package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class PromptModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column( columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String filepath = "prompts/Testprompt.txt";
    public PromptModel(String filepath) {
        this.filepath = filepath;
    }


}
