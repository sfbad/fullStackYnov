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
    @Column(name="id", nullable = false)

    private long id;
    private String description;
    public PromptModel(String promptText) {
        this.description = fil
    }

    private String filepath;


}
