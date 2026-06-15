package com.example.editionlpgm.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "agent_regionale")
@Data
public class AgentMarinRegionale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agent")
    private Long idAgent;

    private String nom;

    private String prenom;

    private String Region;

    @Column(unique = true)
    private String email;

    @Column(name = "mot_de_passe")
    private String motDePasse;



}