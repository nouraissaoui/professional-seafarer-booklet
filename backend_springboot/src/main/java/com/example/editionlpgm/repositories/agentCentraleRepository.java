package com.example.editionlpgm.repositories;

import com.example.editionlpgm.entities.AgentMarinCentrale;
import com.example.editionlpgm.entities.AgentMarinRegionale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface agentCentraleRepository extends JpaRepository<AgentMarinCentrale, Long> {
    Optional<AgentMarinCentrale> findByEmail(String email);
    Optional<AgentMarinCentrale> findByEmailAndMotDePasse(String email, String motDePasse);
}
