package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.Navire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavireRepository extends JpaRepository<Navire, Long> {

    // Exemple : chercher des navires par type
    List<Navire> findByTypeNavire(String typeNavire);

    // Exemple : chercher des navires par propriétaire
    List<Navire> findByProprietaire(String proprietaire);
}
