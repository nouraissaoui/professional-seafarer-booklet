package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.LivretMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LivretMaritimeRepository extends JpaRepository<LivretMaritime, Long> {
    // Tu peux ajouter des méthodes personnalisées, par exemple :
    // Optional<LivretMaritime> findByNumeroLivret(String numeroLivret);
    long countByDateCreation(LocalDate dateCreation);
}