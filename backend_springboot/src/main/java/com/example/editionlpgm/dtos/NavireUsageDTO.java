package com.example.editionlpgm.dtos;

public class NavireUsageDTO {
    private String nomNavire;
    private long nombreUtilisations;

    public NavireUsageDTO(String nomNavire, long nombreUtilisations) {
        this.nomNavire = nomNavire;
        this.nombreUtilisations = nombreUtilisations;
    }

    public String getNomNavire() { return nomNavire; }
    public long getNombreUtilisations() { return nombreUtilisations; }
}
