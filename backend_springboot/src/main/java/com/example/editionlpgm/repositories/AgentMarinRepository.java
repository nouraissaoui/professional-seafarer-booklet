package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.AgentMarinCentrale;
import com.example.editionlpgm.entities.AgentMarinRegionale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentMarinRepository extends JpaRepository<AgentMarinRegionale, Long> {

    // Par expl chercher l'agent par mail
    Optional<AgentMarinRegionale> findByEmail(String email);
    Optional<AgentMarinRegionale> findByEmailAndMotDePasse(String email, String motDePasse);


}