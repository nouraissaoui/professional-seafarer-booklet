package com.example.editionlpgm.dtos;

import com.example.editionlpgm.entities.Embarquement;
import com.example.editionlpgm.entities.Marin;

import java.util.List;

public class MarinAvecEmbarquementsDTO {
    private Marin marin;
    private List<Embarquement> embarquements;

    public MarinAvecEmbarquementsDTO() {
        // constructeur vide nécessaire pour pouvoir instancier sans arguments
    }
    public MarinAvecEmbarquementsDTO(Marin marin, List<Embarquement> embarquements) {
        this.marin = marin;
        this.embarquements = embarquements;
    }

    public Marin getMarin() {
        return marin;
    }

    public void setMarin(Marin marin) {
        this.marin = marin;
    }

    public List<Embarquement> getEmbarquements() {
        return embarquements;
    }

    public void setEmbarquements(List<Embarquement> embarquements) {
        this.embarquements = embarquements;
    }
}
