package com.example.editionlpgm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "agent_centrale")
@Data
public class AgentMarinCentrale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agent")
    private Long idAgentC;

    private String nom;

    private String prenom;

    @Column(unique = true)
    private String email;

    @Column(name = "mot_de_passe")
    private String motDePasse;



}