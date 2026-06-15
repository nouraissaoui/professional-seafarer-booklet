package com.example.editionlpgm;

import com.example.editionlpgm.dtos.AgentLoginRequest;
import com.example.editionlpgm.dtos.LivretMaritimeRequest;
import com.example.editionlpgm.dtos.MarinAvecEmbarquementsDTO;
import com.example.editionlpgm.entities.*;
import com.example.editionlpgm.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/agentmarin")//pour permettre l'acce angular d'envoyer des requete a spring boot
public class LPGMController {
    @Autowired
    private LPGMService agentMarinService;
    @Autowired
    private DemandeLPGMRepository DemandeLPGMRepository;

    @Autowired
    private MarinRepository MarinRepository;
    @Autowired
    private AgentMarinRepository agentMarinRepository;
    @Autowired
    private NavireRepository NavireRepository;
    @Autowired
    private EmbarquementRepository EmbarquementRepository;
    @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody AgentLoginRequest request) {
       try {
           Map<String, Object> response = agentMarinService.authenticate2(request.getEmail(), request.getPassword());
           return ResponseEntity.ok(response);
       } catch (RuntimeException ex) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
       }
   }

    @GetMapping("/demandes/{idAgent}")
    public List<DemandeLPGM> getDemandesByAgent(@PathVariable Long idAgent) {
        return agentMarinService.getDemandesByAgentRegionale(idAgent);
    }
    @GetMapping("/demandes/count/{idAgent}")
    public Map<String, Long> getDemandesCountByEtat(@PathVariable Long idAgent) {
        return agentMarinService.countDemandesTraiteesEtNonTraitees(idAgent);
    }
    @GetMapping("/{idDemande}")
    public ResponseEntity<Marin> getMarinByIdDemande(@PathVariable Long idDemande) {
        Optional<DemandeLPGM> demandeOpt = DemandeLPGMRepository.findById(idDemande);
        if (demandeOpt.isPresent()) {
            Marin marin = demandeOpt.get().getMarin();
            if (marin != null) {
                return ResponseEntity.ok(marin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{idDemande}/type")
    public ResponseEntity<String> getTypeDemandeById(@PathVariable Long idDemande) {
        String typeDemande = agentMarinService.getTypeDemandeById(idDemande);
        if (typeDemande != null) {
            return ResponseEntity.ok(typeDemande);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /*pour la mise a jour des champs du marin  modifiable par l'agent*/
    @PutMapping("/{id}/marin")
    public Marin updateMarin(@PathVariable Long id, @RequestBody Marin marinDetails) {
        Marin marin =MarinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marin introuvable avec id " + id));

        // Mise à jour des champs modifiés
        marin.setNomFr(marinDetails.getNomFr());
        marin.setNomAr(marinDetails.getNomAr());
        marin.setPrenomFr(marinDetails.getPrenomFr());
        marin.setPrenomAr(marinDetails.getPrenomAr());
        marin.setDateNaissance(marinDetails.getDateNaissance());
        marin.setNationaliteFr(marinDetails.getNationaliteFr());
        marin.setNationaliteAr(marinDetails.getNationaliteAr());
        marin.setCin(marinDetails.getCin());
        marin.setCouleurYeuxFr(marinDetails.getCouleurYeuxFr());
        marin.setCouleurYeuxAr(marinDetails.getCouleurYeuxAr());
        marin.setHauteur(marinDetails.getHauteur());
        marin.setCheveuxFr(marinDetails.getCheveuxFr());
        marin.setCheveuxAr(marinDetails.getCheveuxAr());
        marin.setTelephone(marinDetails.getTelephone());
        marin.setEmail(marinDetails.getEmail());
        marin.setLieuNaissanceFr(marinDetails.getLieuNaissanceFr());
        marin.setLieuNaissanceAr(marinDetails.getLieuNaissanceAr());
        marin.setSexe(marinDetails.getSexe());

        return MarinRepository.save(marin);
    }
    /*la mise a jour des attributs de la demande lorsque je clique dur envoyer au centrale*/
    @PostMapping("/{idDemande}/valider")
    public ResponseEntity<DemandeLPGM> validerEtEnvoyer(
            @PathVariable Long idDemande,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("signature") MultipartFile signature,
            @RequestParam("empreinteD") MultipartFile empreinteD,
            @RequestParam("empreinteG") MultipartFile empreinteG
    ) throws IOException {
        DemandeLPGM demande = agentMarinService.sendToCentral(
                idDemande,
                photo.getBytes(),
                signature.getBytes(),
                empreinteD.getBytes(),
                empreinteG.getBytes()
        );
        return ResponseEntity.ok(demande);
    }
    /*bich nista3malha bich yimchi ychouf statuts mta3ha ken traitée y7othom disabled*/
    @GetMapping("demandebyid/{id}")
    public ResponseEntity<DemandeLPGM> getDemandeById(@PathVariable Long id) {
        DemandeLPGM demande = agentMarinService.getDemandeById(id);
        return ResponseEntity.ok(demande);
    }
    /*pour recuperer les photos , les empreinte et la signature des demandes traitée pour qu'il apparait dans consulter*/
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        DemandeLPGM demande = agentMarinService.getDemandeById(id);
        byte[] photo = demande.getPhotoMarin();
        return ResponseEntity.ok().body(photo);
    }

    @GetMapping("/{id}/signature")
    public ResponseEntity<byte[]> getSignature(@PathVariable Long id) {
        DemandeLPGM demande = agentMarinService.getDemandeById(id);
        byte[] signature = demande.getSignatureMarin();
        return ResponseEntity.ok().body(signature);
    }

    @GetMapping("/{id}/empreinte-droite")
    public ResponseEntity<byte[]> getEmpreinteDroite(@PathVariable Long id) {
        DemandeLPGM demande =agentMarinService.getDemandeById(id);
        byte[] empreinteDroite = demande.getEmpreinteDroite();
        return ResponseEntity.ok().body(empreinteDroite);
    }

    @GetMapping("/{id}/empreinte-gauche")
    public ResponseEntity<byte[]> getEmpreinteGauche(@PathVariable Long id) {
        DemandeLPGM demande = agentMarinService.getDemandeById(id);
        byte[] empreinteGauche = demande.getEmpreinteGauche();
        return ResponseEntity.ok().body(empreinteGauche);
    }
    @GetMapping("/traitees/centrale")
    public List<DemandeLPGM> getDemandesTraiteesAuCentrale() {
        return agentMarinService.getDemandesTraiteesAuCentrale();
    }


    @Autowired
    private LivretMaritimeRepository livretRepository;

    @Autowired
    private MarinRepository marinRepository;

    @Autowired
    private DemandeLPGMRepository demandeRepository;

    @Autowired
    private agentCentraleRepository agentRepository;

    /*c'est pour enregistrer le carnet du livret maritime du marin dans la base suite au clique sur le boutton imprimer*/
    @PostMapping("/enregistrer")
    public ResponseEntity<LivretMaritime> enregistrerLivret(@RequestBody LivretMaritimeRequest request) {
        LivretMaritime livret = agentMarinService.enregistrerLivret(request);
        return ResponseEntity.ok(livret);
    }
    @PutMapping("/validerCentrale/{idDemande}")
    public ResponseEntity<Void> validerDemandeCentrale(@PathVariable Long idDemande) {
        agentMarinService.validerDemandeCentrale(idDemande);
        return ResponseEntity.ok().build();
    }
    /*pour connaitre combien du livret a eu etre imprimer */
    @GetMapping("/count-imprimes-aujourdhui")
    public ResponseEntity<Long> getCountLivretsImprimesAujourdHui() {
        long count = agentMarinService.countLivretsImprimesAujourdHui();
        return ResponseEntity.ok(count);
    }



/*c'est pour la notification par email*/
@PostMapping("/{idMarin}/notify-livret")
public ResponseEntity<String> notifierLivretPret(@PathVariable Long idMarin) {
    Marin marin = marinRepository.findById(idMarin)
            .orElseThrow(() -> new RuntimeException("Marin non trouvé"));

    String email = marin.getEmail();
    // Tu peux personnaliser le message ici
    String sujet = "Votre livret maritime est prêt";
    String message = "Bonjour " + marin.getPrenomFr() + ",\n\nVotre livret maritime est prêt à être porté.\n\nCordialement,\nLa Marine";

    agentMarinService.envoyerEmail(email, sujet, message);

    return ResponseEntity.ok("Email envoyé au marin avec succès.");
}
/*pour afficher les marin possedant des livret*/
@GetMapping("/avec-livret")
public ResponseEntity<List<Marin>> getMarinsAvecLivret() {
    return ResponseEntity.ok(MarinRepository.findMarinsAvecLivret());
}
/*c'est pour afficher les marin qui ont des embarquement*/
@GetMapping("/avec-embarquement")
public List<Marin> getMarinsAvecEmbarquements() {
    return agentMarinService.getMarinsAvecEmbarquements();
}
@GetMapping("/marin/{idMarin}")
    public List<Embarquement> getEmbarquementsDuMarin(@PathVariable Long idMarin) {
        return agentMarinService.getEmbarquementsDuMarin(idMarin);
    }
    @GetMapping("/touslesembarquements")
    public List<Embarquement> getAllEmbarquements() {
        return agentMarinService.getAllEmbarquements();
    }
    /*@PostMapping("/ajouter")
    public ResponseEntity<Embarquement> ajouterEmbarquement(@RequestBody Embarquement embarquement) {
        return ResponseEntity.ok(agentMarinService.ajouterEmbarquement(embarquement));
    }*/
    @PostMapping("/ajouter")
    public ResponseEntity<Embarquement> ajouterEmbarquement(@RequestBody Embarquement embarquement) {
        Long idAgent = embarquement.getAgentRegionale().getIdAgent();
        AgentMarinRegionale agent = agentMarinRepository.findById(idAgent)
                .orElseThrow(() -> new RuntimeException("Agent régional non trouvé"));
        embarquement.setAgentRegionale(agent);

        // Pareil pour marin
        Long idMarin = embarquement.getMarin().getIdMarin();
        Marin marin = marinRepository.findById(idMarin)
                .orElseThrow(() -> new RuntimeException("Marin non trouvé"));
        embarquement.setMarin(marin);

        // Pareil pour navire
        Long idNavire = embarquement.getNavire().getIdNavire();
        Navire navire = NavireRepository.findById(idNavire)
                .orElseThrow(() -> new RuntimeException("Navire non trouvé"));
        embarquement.setNavire(navire);

        Embarquement saved = agentMarinService.ajouterEmbarquement(embarquement);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/navires")
    public ResponseEntity<List<Navire>> getNavires() {
        return ResponseEntity.ok(agentMarinService.getAllNavires());
    }
    /*pour selectionner l'historique des embarquement d'un marin*/
    @GetMapping("/marins-avec-embarquements")
    public ResponseEntity<List<MarinAvecEmbarquementsDTO>> getMarinsAvecLeursEmbarquements() {
        List<MarinAvecEmbarquementsDTO> data = agentMarinService.getMarinsAvecLeursEmbarquements();
        return ResponseEntity.ok(data);
    }
    @GetMapping("/marins-avec-embarquements/recherche")
    public ResponseEntity<List<MarinAvecEmbarquementsDTO>> rechercherEmbarquements(
            @RequestParam("query") String query) {
        List<MarinAvecEmbarquementsDTO> result = agentMarinService.rechercherParNomMarinOuNavire(query);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}/embarquements")
    public ResponseEntity<?> getEmbarquementsParMarin(@PathVariable Long id) {
        Optional<Marin> marinOpt = marinRepository.findById(id);
        if (marinOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marin introuvable");
        }

        Marin marin = marinOpt.get();
        List<Embarquement> embarquements = EmbarquementRepository.findByMarinId(id);

        return ResponseEntity.ok(Map.of(
                "marin", marin,
                "embarquements", embarquements
        ));
    }
    @GetMapping("/embarquement/agent/{idAgent}")
    public List<Embarquement> getEmbarquementsForAgent(@PathVariable Long idAgent) {
        return agentMarinService.getEmbarquementsByAgent(idAgent);
    }
    /*pour la mise a jour des champ boutton modifier*/
    @PutMapping("/{id}")
    public ResponseEntity<Embarquement> updateEmbarquement(
            @PathVariable Long id,
            @RequestBody Embarquement embarquement) {

        Embarquement updated = agentMarinService.updateEmbarquement(id, embarquement);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmbarquement(@PathVariable Long id) {
        try {
            agentMarinService.supprimerEmbarquement(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmbarquements", agentMarinService.getTotalEmbarquements());
        stats.put("naviresUsage", agentMarinService.getNaviresUsage());
        stats.put("marinsAvecLivret", agentMarinService.getNombreMarinsAvecLivret());

        return ResponseEntity.ok(stats);
    }
}