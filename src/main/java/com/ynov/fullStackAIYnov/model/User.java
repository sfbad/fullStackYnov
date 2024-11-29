package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private long id;

    @Lob
    @Column(name="response", columnDefinition = "CLOB")
    private String response;

    @OneToOne
    ChatMessageSend chatMessageSend;
}
