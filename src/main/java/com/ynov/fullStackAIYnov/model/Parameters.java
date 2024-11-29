package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Parameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private long id;

    private String nomParam;
    private String TypeParam;

    @ManyToOne
    @JoinColumn(name="user_id")
    private PromptModel promptModel;

}
