package com.example.editionlpgm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "embarquement")
@Data
public class Embarquement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_embarquement")
    private Long idEmbarquement;

    @Column(name = "date_embarquement")
    private LocalDate dateEmbarquement;

    @Column(name = "date_debarquement")
    private LocalDate dateDebarquement;

    @Column(name = "poste_occupe")
    private String posteOccupe;

    @ManyToOne
    @JoinColumn(name = "id_marin")
    private Marin marin;

    @ManyToOne
    @JoinColumn(name = "id_navire")
    private Navire navire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_agent")
    private AgentMarinRegionale agentRegionale;
}