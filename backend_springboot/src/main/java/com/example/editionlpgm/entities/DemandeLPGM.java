package com.example.editionlpgm.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "demande_lpgm")
@Data
public class DemandeLPGM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande")
    private Long idDemande;

    private String typeDemande; // ex: Création, Renouvellement

    private String etatDemande; // ex: En attente, Traité coté regionale

    private String etatDemandeCentrale = "Non Traité";

    private LocalDate dateDemande;

    // ✅ Ajout : Date d'émission au centrale
    private LocalDate dateEmissionCentrale;


    // ✅ Ajout : Empreintes et photo
    @Lob
    @Column(name = "empreinte_droite", columnDefinition = "LONGBLOB")
    private byte[] empreinteDroite;

    @Lob
    @Column(name = "empreinte_gauche", columnDefinition = "LONGBLOB")
    private byte[] empreinteGauche;

    @Lob
    @Column(name = "photo_marin", columnDefinition = "LONGBLOB")
    private byte[] photoMarin;

    // ✅ 📌 Ajout : Signature
    @Lob
    @Column(name = "signature_marin", columnDefinition = "LONGBLOB")
    private byte[] signatureMarin;



    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_marin")
    private Marin marin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_agent_regionale")
    private AgentMarinRegionale agentRegionale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_agent_Centrale")
    private AgentMarinCentrale agentCentrale;
}