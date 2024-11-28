package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
//
public class ChatMessageSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private long id;
    private String question;
    @OneToOne
    PromptModel promptModel;

}
