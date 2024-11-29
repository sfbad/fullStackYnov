package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ChatManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String prompt;
    private String question;
    @Column(columnDefinition = "TEXT")
    private String response;



}
