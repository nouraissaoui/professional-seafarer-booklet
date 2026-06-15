package com.example.editionlpgm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "navire")
@Data
public class Navire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_navire")
    private Long idNavire;

    @Column(name = "nom_navire")
    private String nomNavire;

    @Column(name = "type_navire")
    private String typeNavire;

    private Integer capacite;

    private String proprietaire;
}