package com.example.editionlpgm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "marin")
@Data
public class Marin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marin")
    private Long idMarin;

    @Column(name = "nom_fr")
    private String nomFr;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "prenom_fr")
    private String prenomFr;

    @Column(name = "prenom_ar")
    private String prenomAr;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "nationalite_fr")
    private String nationaliteFr;

    @Column(name = "nationalite_ar")
    private String nationaliteAr;

    @Column(unique = true)
    private String cin;

    @Column(name = "couleur_yeux_fr")
    private String couleurYeuxFr;

    @Column(name = "couleur_yeux_ar")
    private String couleurYeuxAr;

    private Float hauteur;

    @Column(name = "cheveux_fr")
    private String cheveuxFr;

    @Column(name = "cheveux_ar")
    private String cheveuxAr;

    private String telephone;

    private String email;

    @Column(name = "lieu_naissance_fr")
    private String lieuNaissanceFr;

    @Column(name = "lieu_naissance_ar")
    private String lieuNaissanceAr;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    public enum Sexe {
        HOMME,
        FEMME
    }
    @OneToMany(mappedBy = "marin")
    @JsonIgnore
    private List<LivretMaritime> livrets;
}
