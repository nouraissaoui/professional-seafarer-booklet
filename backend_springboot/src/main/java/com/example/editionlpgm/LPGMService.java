package com.example.editionlpgm;

import com.example.editionlpgm.dtos.LivretMaritimeRequest;
import com.example.editionlpgm.dtos.MarinAvecEmbarquementsDTO;
import com.example.editionlpgm.dtos.NavireUsageDTO;
import com.example.editionlpgm.entities.*;
import com.example.editionlpgm.repositories.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
public class LPGMService {
    @Autowired
    private AgentMarinRepository agentMarinRepository;
    @Autowired
    private DemandeLPGMRepository DemandeLPGMRepository;
    @Autowired
    private agentCentraleRepository agentCentraleRepository;
    @Autowired
    private  LivretMaritimeRepository livretRepository;
    @Autowired
    private  MarinRepository marinRepository;
    @Autowired
    private  EmbarquementRepository EmbarquementRepository;
    @Autowired
    private  NavireRepository NavireRepository;



    public AgentMarinRegionale authenticate(String email, String password) {
        return agentMarinRepository.findByEmail(email)
                .filter(agent -> agent.getMotDePasse().equals(password)) // comparaison simple
                .orElse(null);
    }
    public List<DemandeLPGM> getDemandesByAgentRegionale(Long idAgent) {
        return DemandeLPGMRepository.findByAgentRegionaleIdAgent(idAgent);
    }

    public Map<String, Long> countDemandesTraiteesEtNonTraitees(Long idAgent) {
        List<DemandeLPGM> demandes = DemandeLPGMRepository.findByAgentRegionaleIdAgent(idAgent);

        long countNonTraite = demandes.stream()
                .filter(d -> "En attente".equalsIgnoreCase(d.getEtatDemande()))
                .count();

        long countTraite = demandes.stream()
                .filter(d -> "Traité".equalsIgnoreCase(d.getEtatDemande()))
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("Non traité", countNonTraite);
        result.put("Traité", countTraite);

        return result;
    }
    /*pour trouver le type du demande dans le formulaire lorsque je clique sur chercher*/
    public String getTypeDemandeById(Long idDemande) {
        return DemandeLPGMRepository.findById(idDemande)
                .map(DemandeLPGM::getTypeDemande)
                .orElse(null);
    }
    /*public DemandeLPGM sendToCentral(Long idDemande, byte[] photo, byte[] signature, byte[] empreinteD, byte[] empreinteG) {
        DemandeLPGM demande = DemandeLPGMRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        demande.setPhotoMarin(photo);
        demande.setSignatureMarin(signature);
        demande.setEmpreinteDroite(empreinteD);
        demande.setEmpreinteGauche(empreinteG);
        demande.setEtatDemande("Traité");
        demande.setDateEmissionCentrale(LocalDate.now());

        return DemandeLPGMRepository.save(demande);
    }*/

    public DemandeLPGM sendToCentral(Long idDemande, byte[] photo, byte[] signature, byte[] empreinteD, byte[] empreinteG) {
        DemandeLPGM demande = DemandeLPGMRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Mise à jour des fichiers binaires
        demande.setEtatDemande("Traité");
        demande.setDateEmissionCentrale(LocalDate.now());
        demande.setPhotoMarin(photo);
        demande.setSignatureMarin(signature);
        demande.setEmpreinteDroite(empreinteD);
        demande.setEmpreinteGauche(empreinteG);

        // Mise à jour de l'état côté centrale
        demande.setEtatDemandeCentrale("non Traité");

        // Récupérer l'agent centrale avec id=1
        AgentMarinCentrale agentCentrale = agentCentraleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Agent centrale introuvable"));

        // Associer l'agent centrale à la demande
        demande.setAgentCentrale(agentCentrale);

        // Sauvegarder la demande modifiée
        return DemandeLPGMRepository.save(demande);
    }
    public DemandeLPGM getDemandeById(Long id) {
        return DemandeLPGMRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DemandeLPGM non trouvée avec l'id " + id));
    }
    /*c'est pour l'authentification du centrale*/
    /*public Map<String, Object> authenticate2(String email, String motDePasse) {
        Optional<AgentMarinCentrale> centrale = agentCentraleRepository.findByEmailAndMotDePasse(email, motDePasse);
        if (centrale.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("agent", centrale.get());
            response.put("role", "centrale");
            return response;
        }

        Optional<AgentMarinRegionale> regionale = agentMarinRepository.findByEmailAndMotDePasse(email, motDePasse);
        if (regionale.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("agent", regionale.get());
            response.put("role", "regionale");
            return response;
        }

        throw new RuntimeException("Email ou mot de passe invalide");
    }*/
    public Map<String, Object> authenticate2(String email, String motDePasse) {
        Optional<AgentMarinCentrale> centrale = agentCentraleRepository.findByEmailAndMotDePasse(email, motDePasse);
        if (centrale.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("agentCentrale", centrale.get());
            response.put("role", "centrale");
            return response;
        }

        Optional<AgentMarinRegionale> regionale = agentMarinRepository.findByEmailAndMotDePasse(email, motDePasse);
        if (regionale.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("agentRegionale", regionale.get());
            response.put("role", "regionale");
            return response;
        }

        throw new RuntimeException("Email ou mot de passe invalide");
    }
    public List<DemandeLPGM> getDemandesTraiteesAuCentrale() {
        return DemandeLPGMRepository.findByEtatDemandeAndDateEmissionCentraleIsNotNull("Traité");
    }

    public LivretMaritime enregistrerLivret(LivretMaritimeRequest request) {
        LivretMaritime livret = new LivretMaritime();

        livret.setNumeroLivret(request.getNumeroLivret());
        livret.setDateCreation(request.getDateCreation());
        livret.setDateExpiration(request.getDateExpiration());
        livret.setLieuCreation(request.getLieuCreation());
        livret.setType(request.getType());

        Marin marin = marinRepository.findById(request.getIdMarin())
                .orElseThrow(() -> new RuntimeException("Marin non trouvé avec id: " + request.getIdMarin()));
        livret.setMarin(marin);

        DemandeLPGM demande = DemandeLPGMRepository.findById(request.getIdDemande())
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec id: " + request.getIdDemande()));
        livret.setDemandeLPGM(demande);

        AgentMarinCentrale agentCentrale = agentCentraleRepository.findById(request.getIdAgentCentrale())
                .orElseThrow(() -> new RuntimeException("Agent Centrale non trouvé avec id: " + request.getIdAgentCentrale()));
        livret.setAgentCentrale(agentCentrale);

        return livretRepository.save(livret);
    }

    public void validerDemandeCentrale(Long idDemande) {
        DemandeLPGM demande = DemandeLPGMRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec id: " + idDemande));
        demande.setEtatDemandeCentrale("Traité");
        demande.setDateEmissionCentrale(LocalDate.now());
        DemandeLPGMRepository.save(demande);
    }

    public long countLivretsImprimesAujourdHui() {
        LocalDate today = LocalDate.now();
        return livretRepository.countByDateCreation(today);
    }
/*c'est pour notifier le marin que son livret est pret a etre porter*/
    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmail(String to, String sujet, String texte) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ton.email@gmail.com");  // Ajoute bien l'expéditeur explicitement
            message.setTo(to);
            message.setSubject(sujet);
            message.setText(texte);

            mailSender.send(message);
            System.out.println("Email envoyé à " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du mail : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Marin> getMarinsAvecEmbarquements() {
        return marinRepository.findMarinsAvecEmbarquements();
    }
    public List<Embarquement> getEmbarquementsDuMarin(Long idMarin) {
        return EmbarquementRepository.findByMarinId(idMarin);
    }
    public List<Embarquement> getAllEmbarquements() {
        return EmbarquementRepository.findAllWithDetails();
    }
    public List<Navire> getAllNavires() {
        return NavireRepository.findAll();
    }
    public Embarquement ajouterEmbarquement(Embarquement embarquement) {
        return EmbarquementRepository.save(embarquement);
    }
    public List<MarinAvecEmbarquementsDTO> getMarinsAvecLeursEmbarquements() {
        List<Marin> marins = marinRepository.findMarinsAvecEmbarquements();
        List<MarinAvecEmbarquementsDTO> resultat = new ArrayList<>();

        for (Marin marin : marins) {
            List<Embarquement> historique = EmbarquementRepository
                    .findByMarinOrderByDateEmbarquementDesc(marin);
            resultat.add(new MarinAvecEmbarquementsDTO(marin, historique));
        }
        return resultat;
    }
    public List<MarinAvecEmbarquementsDTO> rechercherParNomMarinOuNavire(String query) {
        // Préparer la recherche insensible à la casse
        String likeQuery = "%" + query.toLowerCase() + "%";

        // Récupérer les embarquements filtrés
        List<Embarquement> filtered = EmbarquementRepository.findByNomMarinOuNomNavire(likeQuery);

        // Grouper par marin
        Map<Long, MarinAvecEmbarquementsDTO> map = new LinkedHashMap<>();
        for (Embarquement e : filtered) {
            Long idMarin = e.getMarin().getIdMarin();
            map.computeIfAbsent(idMarin, id -> {
                MarinAvecEmbarquementsDTO dto = new MarinAvecEmbarquementsDTO();
                dto.setMarin(e.getMarin());
                dto.setEmbarquements(new ArrayList<>());
                return dto;
            }).getEmbarquements().add(e);
        }
        return new ArrayList<>(map.values());
    }
    public List<Embarquement> getEmbarquementsByAgent(Long idAgent) {
        return EmbarquementRepository.findByAgentRegionaleIdAgent(idAgent);
    }
    /*pour la mise a jour dans la consultation*/
    /*public Embarquement updateEmbarquement(Long id, Embarquement updatedEmbarquement) {
        Embarquement existing = EmbarquementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Embarquement non trouvé"));

        // Mettre à jour les infos embarquement
        existing.setDateEmbarquement(updatedEmbarquement.getDateEmbarquement());
        existing.setDateDebarquement(updatedEmbarquement.getDateDebarquement());
        existing.setPosteOccupe(updatedEmbarquement.getPosteOccupe());

        // Mettre à jour le navire
        if (updatedEmbarquement.getNavire() != null) {
            Navire updatedNavire = updatedEmbarquement.getNavire();
            Navire navireExistant = existing.getNavire();

            if (navireExistant != null && navireExistant.getIdNavire().equals(updatedNavire.getIdNavire())) {
                navireExistant.setNomNavire(updatedNavire.getNomNavire());
                navireExistant.setTypeNavire(updatedNavire.getTypeNavire());
                navireExistant.setProprietaire(updatedNavire.getProprietaire());
                navireExistant.setCapacite(updatedNavire.getCapacite());

                NavireRepository.save(navireExistant);
            } else {
                // Si changement de navire lié
                existing.setNavire(updatedNavire);
            }
        }

        return EmbarquementRepository.save(existing);
    }*/
    public Embarquement updateEmbarquement(Long id, Embarquement updatedEmbarquement) {
        Embarquement existing = EmbarquementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Embarquement non trouvé"));

        // 🔹 Mettre à jour les infos de l'embarquement
        existing.setDateEmbarquement(updatedEmbarquement.getDateEmbarquement());
        existing.setDateDebarquement(updatedEmbarquement.getDateDebarquement());
        existing.setPosteOccupe(updatedEmbarquement.getPosteOccupe());

        // 🔹 Mettre à jour l'agent occupé (agentRegionale)
        if (updatedEmbarquement.getAgentRegionale() != null &&
                updatedEmbarquement.getAgentRegionale().getIdAgent() != null) {
            existing.setAgentRegionale(updatedEmbarquement.getAgentRegionale());
        }

        // 🔹 Mettre à jour le navire
        if (updatedEmbarquement.getNavire() != null) {
            Navire updatedNavire = updatedEmbarquement.getNavire();
            Navire navireExistant = existing.getNavire();

            if (navireExistant != null && navireExistant.getIdNavire().equals(updatedNavire.getIdNavire())) {
                navireExistant.setNomNavire(updatedNavire.getNomNavire());
                navireExistant.setTypeNavire(updatedNavire.getTypeNavire());
                navireExistant.setProprietaire(updatedNavire.getProprietaire());
                navireExistant.setCapacite(updatedNavire.getCapacite());

                NavireRepository.save(navireExistant);
            } else {
                // Si changement de navire lié
                existing.setNavire(updatedNavire);
            }
        }

        return EmbarquementRepository.save(existing);
    }
    /*pour la suppression d'un embarquement*/
    public void supprimerEmbarquement(Long id) {
        Embarquement embarquement = EmbarquementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Embarquement non trouvé"));
        EmbarquementRepository.delete(embarquement);
    }
/*pour la visualisation des statisqtique*/
// Nombre total d'embarquements
public long getTotalEmbarquements() {
    return EmbarquementRepository.count();
}

    // Nombre d'utilisation des navires (nombre d'embarquement par navire)
    public List<NavireUsageDTO> getNaviresUsage() {
        return EmbarquementRepository.countEmbarquementsByNavire();
    }

    // Nombre total de marins avec livret maritime
    public LPGMService(MarinRepository marinRepository) {
        this.marinRepository = marinRepository;
    }

    public long getNombreMarinsAvecLivret() {
        return marinRepository.countMarinsWithLivrets();
    }




}