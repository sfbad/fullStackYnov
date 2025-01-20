package com.ynov.fullStackAIYnov.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personnage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomPersonnage;


    @ElementCollection
    private List<String> traitsPersonnalite;
    private String description;
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

}
