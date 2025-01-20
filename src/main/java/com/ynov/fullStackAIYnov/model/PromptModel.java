package com.ynov.fullStackAIYnov.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PromptModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String imgUrl ;
}
