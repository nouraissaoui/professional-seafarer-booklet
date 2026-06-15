package com.example.editionlpgm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "livret_maritime")
@Data
public class LivretMaritime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livret")
    private Long idLivret;

    @Column(name = "numero_livret", unique = true)
    private String numeroLivret;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "lieu_creation")
    private String lieuCreation;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    private String type;

    @ManyToOne
    @JoinColumn(name = "id_marin")
    private Marin marin;

    @ManyToOne
    @JoinColumn(name = "id_demande")
    private DemandeLPGM demandeLPGM;

    @ManyToOne
    @JoinColumn(name = "id_agent_centrale")
    private AgentMarinCentrale agentCentrale;
}
