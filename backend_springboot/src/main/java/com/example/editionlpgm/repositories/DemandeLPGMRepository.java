package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.DemandeLPGM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeLPGMRepository extends JpaRepository<DemandeLPGM, Long> {
    List<DemandeLPGM> findByAgentRegionaleIdAgent(Long idAgentRegionale);
    // Trouve toutes les demandes où état = "Traité" et dateEmissionCentrale non nulle
    List<DemandeLPGM> findByEtatDemandeAndDateEmissionCentraleIsNotNull(String etatDemande);

}