package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.ai.ollama.OllamaChatModel;
@Entity
@Data
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ido", nullable = false)
    private Long id;
    private User user;
    private String password;

}
