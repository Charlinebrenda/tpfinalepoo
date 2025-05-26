package com.projetpoo.demotpfinale.modele;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.projetpoo.demotpfinale.Exception.CapaciteMaxAtteinteException;
import com.projetpoo.demotpfinale.Exception.EvenementDejaExistantException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConferenceImpl.class, name = "Conference"),
        @JsonSubTypes.Type(value = ConcertImpl.class, name = "Concert")
})
public abstract class Evenement {
    private String id;
    private String nom;

    // Format de date amélioré pour la sérialisation JSON
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String lieu;
    private int capaciteMax;
    private List<String> participants;
    private boolean annule;

    public Evenement() {
        // Constructeur par défaut pour Jackson
        this.participants = new ArrayList<>();
        this.annule = false;
    }

    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.participants = new ArrayList<>();
        this.annule = false;
    }

    @JsonProperty("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @JsonProperty("nom")
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @JsonProperty("date")
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    @JsonProperty("lieu")
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    @JsonProperty("capaciteMax")
    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

    @JsonProperty("participants")
    public List<String> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        return participants;
    }
    public void setParticipants(List<String> participants) {
        this.participants = participants != null ? participants : new ArrayList<>();
    }

    @JsonProperty("annule")
    public boolean isAnnule() { return annule; }
    public void setAnnule(boolean annule) { this.annule = annule; }

    //methode ajouter participant
    public void ajouterparticipant(String participant) throws CapaciteMaxAtteinteException {
        if (annule) {
            System.out.println("L'événement " + nom + " est annulé");
            return;
        }

        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("L'événement " + nom + " a atteint le maximum de participants");
        }

        if (participants.contains(participant)) {
            System.out.println("Le participant " + participant + " est déjà inscrit");
            return;
        }

        participants.add(participant);
        System.out.println("Le participant " + participant + " a été ajouté avec succès");
    }

    //methode annuler un évenement
    public void annuler() throws EvenementDejaExistantException {
        this.annule = true;
        throw new EvenementDejaExistantException("L'événement " + nom + " a été annulé.");
    }

    //methode afficher
    public void afficherDetails() {
        System.out.println("=== DÉTAILS DE L'ÉVÉNEMENT ===");
        System.out.println("ID : " + id);
        System.out.println("Nom : " + nom);
        System.out.println("Date : " + (date != null ? date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Non définie"));
        System.out.println("Lieu : " + lieu);
        System.out.println("Capacité maximale : " + capaciteMax);
        System.out.println("Participants inscrits : " + getParticipants().size() + "/" + capaciteMax);
        System.out.println("Statut : " + (annule ? "ANNULÉ" : "ACTIF"));

        if (!getParticipants().isEmpty()) {
            System.out.println("Liste des participants :");
            for (int i = 0; i < getParticipants().size(); i++) {
                System.out.println((i + 1) + ". " + getParticipants().get(i));
            }
        } else {
            System.out.println("Aucun participant inscrit");
        }
        System.out.println("================================");
    }
}