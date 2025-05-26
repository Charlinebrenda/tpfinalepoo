package com.projetpoo.demotpfinale.functions;

import com.projetpoo.demotpfinale.Exception.CapaciteMaxAtteinteException;
import com.projetpoo.demotpfinale.modele.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class InterfaceInscription extends VBox {

    // Contrôles de l'interface
    private TextField txtNomParticipant;
    private TextField txtEmailParticipant;
    private ComboBox<String> cmbEvenements;
    private ListView<String> listParticipants;
    private Label lblStatut;
    private Label lblDetailsEvenement;

    // Données
    private Map<String, Evenement> evenements;
    private GestionEvenement gestionEvenement;

    public InterfaceInscription() {
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f8f9fa;");

        // Initialisation des données de test
        initialiserDonneesTest();

        // Construction de l'interface
        construireInterface();

        // Mise à jour initiale
        mettreAJourListeEvenements();
    }

    private void initialiserDonneesTest() {
        evenements = new HashMap<>();

        // Création d'événements de test
        ConferenceConcrete conf1 = new ConferenceConcrete(
                "CONF001",
                "JavaFX Workshop",
                LocalDateTime.now().plusDays(30),
                "Salle A - Campus Tech",
                50,
                "Développement Interface",
                new ArrayList<>(Arrays.asList("Dr. Smith", "Prof. Martin"))
        );

        ConferenceConcrete conf2 = new ConferenceConcrete(
                "CONF002",
                "IA et Machine Learning",
                LocalDateTime.now().plusDays(45),
                "Auditorium Principal",
                100,
                "Intelligence Artificielle",
                new ArrayList<>(Arrays.asList("Dr. Johnson", "Prof. Garcia"))
        );

        ConcertConcret concert1 = new ConcertConcret(
                "CONC001",
                "Festival Jazz 2025",
                LocalDateTime.now().plusDays(60),
                "Parc Central",
                200,
                "Miles Davis Tribute",
                "Jazz"
        );

        evenements.put(conf1.getId(), conf1);
        evenements.put(conf2.getId(), conf2);
        evenements.put(concert1.getId(), concert1);

        gestionEvenement = GestionEvenement.getInstance(evenements);
    }

    private void construireInterface() {
        // Titre
        Label titre = new Label("Inscription des Participants");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);
        titre.setAlignment(Pos.CENTER);

        // Section inscription
        VBox sectionInscription = creerSectionInscription();

        // Section liste des participants
        VBox sectionListe = creerSectionListeParticipants();

        // Messages de statut
        lblStatut = new Label();
        lblStatut.setWrapText(true);
        lblStatut.setFont(Font.font("Arial", 12));

        this.getChildren().addAll(titre, sectionInscription, sectionListe, lblStatut);
    }

    private VBox creerSectionInscription() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8;");

        Label titreSection = new Label("Nouvelle Inscription");
        titreSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titreSection.setTextFill(Color.DARKGREEN);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(15, 0, 0, 0));

        // Nom du participant
        grid.add(new Label("Nom du participant:"), 0, 0);
        txtNomParticipant = new TextField();
        txtNomParticipant.setPromptText("Nom complet du participant");
        txtNomParticipant.setPrefWidth(250);
        grid.add(txtNomParticipant, 1, 0);

        // Email du participant
        grid.add(new Label("Email:"), 0, 1);
        txtEmailParticipant = new TextField();
        txtEmailParticipant.setPromptText("email@exemple.com");
        txtEmailParticipant.setPrefWidth(250);
        grid.add(txtEmailParticipant, 1, 1);

        // Sélection d'événement
        grid.add(new Label("Événement:"), 0, 2);
        cmbEvenements = new ComboBox<>();
        cmbEvenements.setPromptText("Sélectionnez un événement");
        cmbEvenements.setPrefWidth(250);
        cmbEvenements.setOnAction(e -> afficherDetailsEvenement());
        grid.add(cmbEvenements, 1, 2);

        // Détails de l'événement sélectionné
        lblDetailsEvenement = new Label();
        lblDetailsEvenement.setWrapText(true);
        lblDetailsEvenement.setFont(Font.font("Arial", 11));
        lblDetailsEvenement.setTextFill(Color.GRAY);
        lblDetailsEvenement.setPrefWidth(400);
        grid.add(lblDetailsEvenement, 0, 3, 2, 1);

        // Boutons
        HBox boutons = new HBox(10);
        boutons.setAlignment(Pos.CENTER_LEFT);
        boutons.setPadding(new Insets(15, 0, 0, 0));

        Button btnInscrire = new Button("Inscrire Participant");
        btnInscrire.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 8 16;");
        btnInscrire.setOnAction(e -> {
            try {
                inscrireParticipant();
            } catch (CapaciteMaxAtteinteException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnEffacer = new Button("Effacer");
        btnEffacer.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 8 16;");
        btnEffacer.setOnAction(e -> effacerChamps());

        boutons.getChildren().addAll(btnInscrire, btnEffacer);

        section.getChildren().addAll(titreSection, grid, boutons);
        return section;
    }

    private VBox creerSectionListeParticipants() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8;");

        Label titreSection = new Label("Participants Inscrits");
        titreSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titreSection.setTextFill(Color.DARKGREEN);

        listParticipants = new ListView<>();
        listParticipants.setPrefHeight(200);
        listParticipants.setStyle("-fx-border-color: #dee2e6; -fx-border-radius: 4;");

        Button btnActualiser = new Button("Actualiser Liste");
        btnActualiser.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 6 12;");
        btnActualiser.setOnAction(e -> actualiserListe());

        section.getChildren().addAll(titreSection, listParticipants, btnActualiser);
        return section;
    }

    private void mettreAJourListeEvenements() {
        ObservableList<String> items = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Evenement evt : evenements.values()) {
            if (!evt.isAnnule()) {
                String item = String.format("%s - %s (%s)",
                        evt.getId(),
                        evt.getNom(),
                        evt.getDate().format(formatter));
                items.add(item);
            }
        }

        cmbEvenements.setItems(items);
    }

    private void afficherDetailsEvenement() {
        String selection = cmbEvenements.getValue();
        if (selection != null) {
            String id = selection.split(" - ")[0];
            Evenement evt = evenements.get(id);
            if (evt != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
                String details = String.format(
                        "📍 Lieu: %s | 📅 Date: %s\n👥 Participants: %d/%d | 🎯 Statut: %s",
                        evt.getLieu(),
                        evt.getDate().format(formatter),
                        evt.getParticipants().size(),
                        evt.getCapaciteMax(),
                        evt.isAnnule() ? "Annulé" : "Actif"
                );
                lblDetailsEvenement.setText(details);
            }
        } else {
            lblDetailsEvenement.setText("");
        }
    }

    private void inscrireParticipant() throws CapaciteMaxAtteinteException {
        // Validation des champs
        if (!validerSaisie()) {
            return;
        }

        String nom = txtNomParticipant.getText().trim();
        String email = txtEmailParticipant.getText().trim();
        String selection = cmbEvenements.getValue();
        String idEvenement = selection.split(" - ")[0];

        Evenement evenement = evenements.get(idEvenement);
        if (evenement != null) {
            // Création d'un identifiant unique pour le participant
            String participantInfo = nom + " (" + email + ")";

            // Tentative d'inscription
            evenement.ajouterparticipant(participantInfo);

            // Mise à jour de l'affichage
            afficherMessage("✅ Participant inscrit avec succès à l'événement " + evenement.getNom(), Color.GREEN);
            actualiserListe();
            afficherDetailsEvenement(); // Mise à jour des détails
            effacerChamps();
        }
    }

    private boolean validerSaisie() {
        StringBuilder erreurs = new StringBuilder();

        if (txtNomParticipant.getText().trim().isEmpty()) {
            erreurs.append("• Le nom du participant est obligatoire\n");
        }

        if (txtEmailParticipant.getText().trim().isEmpty()) {
            erreurs.append("• L'email est obligatoire\n");
        } else if (!isEmailValide(txtEmailParticipant.getText().trim())) {
            erreurs.append("• L'email n'est pas valide\n");
        }

        if (cmbEvenements.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un événement\n");
        }

        if (erreurs.length() > 0) {
            afficherMessage("❌ Erreurs de validation :\n" + erreurs.toString(), Color.RED);
            return false;
        }

        return true;
    }

    private boolean isEmailValide(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private void actualiserListe() {
        ObservableList<String> participants = FXCollections.observableArrayList();

        for (Evenement evt : evenements.values()) {
            if (!evt.getParticipants().isEmpty()) {
                participants.add("=== " + evt.getNom() + " ===");
                for (String participant : evt.getParticipants()) {
                    participants.add("  • " + participant);
                }
                participants.add(""); // Ligne vide pour la séparation
            }
        }

        if (participants.isEmpty()) {
            participants.add("Aucun participant inscrit pour le moment");
        }

        listParticipants.setItems(participants);
    }

    private void effacerChamps() {
        txtNomParticipant.clear();
        txtEmailParticipant.clear();
        cmbEvenements.setValue(null);
        lblDetailsEvenement.setText("");
    }

    private void afficherMessage(String message, Color couleur) {
        lblStatut.setText(message);
        lblStatut.setTextFill(couleur);
    }

    // Classes concrètes pour les tests
    private static class ConferenceConcrete extends Conference {
        public ConferenceConcrete(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String theme, ArrayList<String> intervenants) {
            super(id, nom, date, lieu, capaciteMax, theme, intervenants);
        }
    }

    private static class ConcertConcret extends Concert {
        public ConcertConcret(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String artiste, String genreMusical) {
            super(id, nom, date, lieu, capaciteMax, artiste, genreMusical);
        }
    }
}