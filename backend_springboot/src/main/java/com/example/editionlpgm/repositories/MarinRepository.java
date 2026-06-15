package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.Marin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarinRepository extends JpaRepository<Marin, Long> {
    Optional<Marin> findByIdMarin(Long idMarin);

    // Exemple : chercher un marin par CIN (unique)
    Optional<Marin> findByCin(String cin);
    @Query("SELECT DISTINCT m FROM Marin m JOIN LivretMaritime l ON l.marin = m")
    List<Marin> findMarinsAvecLivret();
    @Query("SELECT DISTINCT e.marin FROM Embarquement e WHERE e.marin IS NOT NULL")
    List<Marin> findMarinsAvecEmbarquements();

    @Query("SELECT COUNT(DISTINCT m) FROM Marin m JOIN m.livrets l")
    long countMarinsWithLivrets();

    // Tu peux ajouter d'autres méthodes personnalisées ici
}