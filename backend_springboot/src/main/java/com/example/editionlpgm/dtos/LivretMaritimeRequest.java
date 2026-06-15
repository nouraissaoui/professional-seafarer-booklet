package com.example.editionlpgm.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LivretMaritimeRequest {

    private String numeroLivret;
    private LocalDate dateCreation;
    private String lieuCreation;
    private LocalDate dateExpiration;
    private String type;

    private Long idMarin;
    private Long idDemande;
    private Long idAgentCentrale;

    // getters et setters

    public String getNumeroLivret() {
        return numeroLivret;
    }

    public void setNumeroLivret(String numeroLivret) {
        this.numeroLivret = numeroLivret;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getLieuCreation() {
        return lieuCreation;
    }

    public void setLieuCreation(String lieuCreation) {
        this.lieuCreation = lieuCreation;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdMarin() {
        return idMarin;
    }

    public void setIdMarin(Long idMarin) {
        this.idMarin = idMarin;
    }

    public Long getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    public Long getIdAgentCentrale() {
        return idAgentCentrale;
    }

    public void setIdAgentCentrale(Long idAgentCentrale) {
        this.idAgentCentrale = idAgentCentrale;
    }

}