package com.example.editionlpgm.repositories;

import com.example.editionlpgm.dtos.NavireUsageDTO;
import com.example.editionlpgm.entities.Embarquement;
import com.example.editionlpgm.entities.Marin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmbarquementRepository extends JpaRepository<Embarquement, Long> {
    @Query("SELECT e FROM Embarquement e LEFT JOIN FETCH e.navire LEFT JOIN FETCH e.agentRegionale WHERE e.marin.idMarin = :idMarin")
    List<Embarquement> findByMarinId(@Param("idMarin") Long idMarin);

    @Query("SELECT e FROM Embarquement e " +
            "LEFT JOIN FETCH e.marin " +
            "LEFT JOIN FETCH e.navire " +
            "LEFT JOIN FETCH e.agentRegionale")
    List<Embarquement> findAllWithDetails();
    List<Embarquement> findByMarinOrderByDateEmbarquementDesc(Marin marin);
    @Query("SELECT e FROM Embarquement e JOIN FETCH e.marin m JOIN FETCH e.navire n ORDER BY m.idMarin, e.dateEmbarquement DESC")
    List<Embarquement> findAllWithMarinAndNavireSorted();
    /*pour la section du recherche*/
    @Query("SELECT e FROM Embarquement e JOIN e.marin m JOIN e.navire n " +
            "WHERE LOWER(m.nomFr) LIKE :query OR LOWER(m.prenomFr) LIKE :query OR LOWER(n.nomNavire) LIKE :query " +
            "ORDER BY e.dateEmbarquement DESC")
    List<Embarquement> findByNomMarinOuNomNavire(@Param("query") String query);
    List<Embarquement> findByAgentRegionaleIdAgent(Long idAgent);

    @Query("SELECT new com.example.editionlpgm.dtos.NavireUsageDTO(e.navire.nomNavire, COUNT(e)) " +
            "FROM Embarquement e GROUP BY e.navire.nomNavire")
    List<NavireUsageDTO> countEmbarquementsByNavire();


    // (optionnel) utile si tu veux regrouper via l'ID directement
    // List<Embarquement> findByMarinIdOrderByDateEmbarquementDesc(Long idMarin);
}
    // Tu peux ajouter des méthodes personnalisées si besoin, par exemple :
    // List<Embarquement> findByMarinId(Long marinId);

