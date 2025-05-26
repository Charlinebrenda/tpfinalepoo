package com.projetpoo.demotpfinale.functions;

public class Inscrireparticipant {
    /*package com.projetpoo.demotpfinale.Interface;
import com.projetpoo.demotpfinale.Exception.EvenementDejaExistantException;
import com.projetpoo.demotpfinale.modele.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.projetpoo.demotpfinale.Exception.CapaciteMaxAtteinteException;
import com.projetpoo.demotpfinale.Exception.EvenementDejaExistantException;
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

public class InterfaceGestionOrganisateur extends VBox {

    // Contrôles de l'interface
    private TextField txtNomEvenement;
    private TextField txtLieu;
    private TextField txtCapacite;
    private DatePicker datePicker;
    private ComboBox<String> cmbHeures;
    private ComboBox<String> cmbMinutes;
    private ComboBox<String> cmbTypeEvenement;
    private TextField txtSpecialite; // Thème ou artiste selon le type
    private TextField txtGenreMusical; // Pour les concerts
    private TextArea txtIntervenants; // Pour les conférences
    private ListView<String> listEvenements;
    private TextArea txtDetailsEvenement;
    private Label lblStatut;
    private TextField txtNouveauParticipant;

    // Données
    private Map<String, Evenement> evenements;
    private GestionEvenement gestionEvenement;
    private Organisateur organisateurConnecte;
    private InterfaceNotifications interfaceNotifications;

    public InterfaceGestionOrganisateur(Organisateur organisateur) {
        this.organisateurConnecte = organisateur;
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f8f9fa;");

        // Initialisation des données
        evenements = new HashMap<>();
        gestionEvenement = GestionEvenement.getInstance(evenements);
        interfaceNotifications = new InterfaceNotifications();

        // Construction de l'interface
        construireInterface();

        // Mise à jour initiale
        actualiserListeEvenements();
    }
    public void afficherInterface(Organisateur organisateur, Stage stagePrincipal) {
        Scene scene = new Scene(this, 900, 700);
        stagePrincipal.setTitle("Gestion des Événements");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }
    private void construireInterface() {
        // Titre avec nom de l'organisateur
        VBox headerBox = new VBox(5);
        Label titre = new Label("Gestion des Événements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);

        Label organisateurLabel = new Label("Organisateur: " + organisateurConnecte.getNom());
        organisateurLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        organisateurLabel.setTextFill(Color.GRAY);

        headerBox.getChildren().addAll(titre, organisateurLabel);
        headerBox.setAlignment(Pos.CENTER);

        // Section creation/modification
        VBox sectionGestion = creerSectionGestion();

        // Section liste et détails des événements
        HBox sectionPrincipale = new HBox(15);
        VBox sectionListe = creerSectionListeEvenements();
        VBox sectionDetails = creerSectionDetails();
        sectionPrincipale.getChildren().addAll(sectionListe, sectionDetails);

        // Messages de statut
        lblStatut = new Label();
        lblStatut.setWrapText(true);
        lblStatut.setFont(Font.font("Arial", 12));
        lblStatut.setStyle("-fx-padding: 10; -fx-background-color: #e9ecef; -fx-background-radius: 5;");

        this.getChildren().addAll(headerBox, sectionGestion, sectionPrincipale, lblStatut);
    }

    private VBox creerSectionGestion() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");

        Label titreSection = new Label("Créer/Modifier un Événement");
        titreSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titreSection.setTextFill(Color.DARKGREEN);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(15, 0, 0, 0));

        // Nom de l'événement
        grid.add(new Label("Nom de l'événement:"), 0, 0);
        txtNomEvenement = new TextField();
        txtNomEvenement.setPromptText("Nom de l'événement");
        txtNomEvenement.setPrefWidth(250);
        grid.add(txtNomEvenement, 1, 0);

        // Lieu
        grid.add(new Label("Lieu:"), 0, 1);
        txtLieu = new TextField();
        txtLieu.setPromptText("Lieu de l'événement");
        txtLieu.setPrefWidth(250);
        grid.add(txtLieu, 1, 1);

        // Date et heure
        grid.add(new Label("Date:"), 0, 2);
        HBox dateTimeBox = new HBox(5);
        datePicker = new DatePicker();
        datePicker.setPrefWidth(150);

        cmbHeures = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            cmbHeures.getItems().add(String.format("%02d", i));
        }
        cmbHeures.setPromptText("HH");
        cmbHeures.setPrefWidth(50);

        cmbMinutes = new ComboBox<>();
        for (int i = 0; i < 60; i += 15) {
            cmbMinutes.getItems().add(String.format("%02d", i));
        }
        cmbMinutes.setPromptText("MM");
        cmbMinutes.setPrefWidth(50);

        dateTimeBox.getChildren().addAll(datePicker, new Label(":"), cmbHeures, new Label(":"), cmbMinutes);
        grid.add(dateTimeBox, 1, 2);

        // Capacité
        grid.add(new Label("Capacité max:"), 0, 3);
        txtCapacite = new TextField();
        txtCapacite.setPromptText("Nombre maximum de participants");
        txtCapacite.setPrefWidth(250);
        grid.add(txtCapacite, 1, 3);

        // Type d'événement
        grid.add(new Label("Type d'événement:"), 0, 4);
        cmbTypeEvenement = new ComboBox<>();
        cmbTypeEvenement.getItems().addAll("Conference", "Concert");
        cmbTypeEvenement.setPromptText("Sélectionnez un type");
        cmbTypeEvenement.setPrefWidth(250);
        cmbTypeEvenement.setOnAction(e -> mettreAJourChampsSpecialises());
        grid.add(cmbTypeEvenement, 1, 4);

        // Champ spécialisé principal
        grid.add(new Label("Thème/Artiste:"), 0, 5);
        txtSpecialite = new TextField();
        txtSpecialite.setPromptText("Thème de conférence ou nom d'artiste");
        txtSpecialite.setPrefWidth(250);
        grid.add(txtSpecialite, 1, 5);

        // Genre musical (pour concerts)
        grid.add(new Label("Genre musical:"), 0, 6);
        txtGenreMusical = new TextField();
        txtGenreMusical.setPromptText("Genre musical (pour concerts)");
        txtGenreMusical.setPrefWidth(250);
        txtGenreMusical.setVisible(false);
        grid.add(txtGenreMusical, 1, 6);

        // Intervenants (pour conférences)
        grid.add(new Label("Intervenants:"), 0, 7);
        txtIntervenants = new TextArea();
        txtIntervenants.setPromptText("Liste des intervenants (un par ligne)");
        txtIntervenants.setPrefWidth(250);
        txtIntervenants.setPrefHeight(60);
        txtIntervenants.setVisible(false);
        grid.add(txtIntervenants, 1, 7);

        // Boutons
        HBox boutons = new HBox(10);
        boutons.setAlignment(Pos.CENTER_LEFT);
        boutons.setPadding(new Insets(15, 0, 0, 0));

        Button btnCreer = new Button("Créer Événement");
        btnCreer.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 8 16;");
        btnCreer.setOnAction(e -> creerEvenement());

        Button btnModifier = new Button("Modifier");
        btnModifier.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-size: 12; -fx-padding: 8 16;");
        btnModifier.setOnAction(e -> modifierEvenement());

        Button btnAnnuler = new Button("Annuler Événement");
        btnAnnuler.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 8 16;");
        btnAnnuler.setOnAction(e -> annulerEvenement());

        Button btnEffacer = new Button("Effacer Champs");
        btnEffacer.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 8 16;");
        btnEffacer.setOnAction(e -> effacerChamps());

        boutons.getChildren().addAll(btnCreer, btnModifier, btnAnnuler, btnEffacer);

        section.getChildren().addAll(titreSection, grid, boutons);
        return section;
    }

    private VBox creerSectionListeEvenements() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");
        section.setPrefWidth(400);

        Label titreSection = new Label("Mes Événements");
        titreSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titreSection.setTextFill(Color.DARKGREEN);

        listEvenements = new ListView<>();
        listEvenements.setPrefHeight(300);
        listEvenements.setStyle("-fx-border-color: #dee2e6; -fx-border-radius: 4;");
        listEvenements.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chargerDetailsEvenement(newVal);
            }
        });

        Button btnActualiser = new Button("Actualiser Liste");
        btnActualiser.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-size: 11; -fx-padding: 5 10;");
        btnActualiser.setOnAction(e -> actualiserListeEvenements());

        section.getChildren().addAll(titreSection, listEvenements, btnActualiser);
        return section;
    }

    private VBox creerSectionDetails() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");
        section.setPrefWidth(400);

        Label titreSection = new Label("Détails de l'Événement");
        titreSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titreSection.setTextFill(Color.DARKGREEN);

        txtDetailsEvenement = new TextArea();
        txtDetailsEvenement.setPrefHeight(200);
        txtDetailsEvenement.setEditable(false);
        txtDetailsEvenement.setStyle("-fx-border-color: #dee2e6; -fx-border-radius: 4;");

        // Section gestion des participants
        Label lblParticipants = new Label("Gestion des Participants");
        lblParticipants.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox ajoutParticipant = new HBox(5);
        txtNouveauParticipant = new TextField();
        txtNouveauParticipant.setPromptText("Email du participant");
        txtNouveauParticipant.setPrefWidth(250);

        Button btnAjouterParticipant = new Button("Ajouter");
        btnAjouterParticipant.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 11; -fx-padding: 5 10;");
        btnAjouterParticipant.setOnAction(e -> ajouterParticipant());

        ajoutParticipant.getChildren().addAll(txtNouveauParticipant, btnAjouterParticipant);

        section.getChildren().addAll(titreSection, txtDetailsEvenement, lblParticipants, ajoutParticipant);
        return section;
    }

    private void mettreAJourChampsSpecialises() {
        String type = cmbTypeEvenement.getValue();
        if (type != null) {
            if (type.equals("Conference")) {
                txtSpecialite.setPromptText("Thème de la conférence");
                txtGenreMusical.setVisible(false);
                txtIntervenants.setVisible(true);
            } else if (type.equals("Concert")) {
                txtSpecialite.setPromptText("Nom de l'artiste");
                txtGenreMusical.setVisible(true);
                txtIntervenants.setVisible(false);
            }
        }
    }

    private void actualiserListeEvenements() {
        ObservableList<String> items = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Evenement evt : evenements.values()) {
            String statut = evt.isAnnule() ? "[ANNULÉ] " : "";
            String item = String.format("%s%s - %s (%s) - %d/%d participants",
                    statut,
                    evt.getId(),
                    evt.getNom(),
                    evt.getDate().format(formatter),
                    evt.getParticipants().size(),
                    evt.getCapaciteMax());
            items.add(item);
        }

        listEvenements.setItems(items);
        afficherMessage("📋 Liste des événements actualisée (" + items.size() + " événements)", Color.BLUE);
    }

    private void chargerDetailsEvenement(String selection) {
        String id = selection.split(" - ")[0].replace("[ANNULÉ] ", "");
        Evenement evt = evenements.get(id);

        if (evt != null) {
            // Charger dans les champs de modification
            txtNomEvenement.setText(evt.getNom());
            txtLieu.setText(evt.getLieu());
            datePicker.setValue(evt.getDate().toLocalDate());
            cmbHeures.setValue(String.format("%02d", evt.getDate().getHour()));
            cmbMinutes.setValue(String.format("%02d", evt.getDate().getMinute()));
            txtCapacite.setText(String.valueOf(evt.getCapaciteMax()));

            if (evt instanceof Conference) {
                Conference conf = (Conference) evt;
                cmbTypeEvenement.setValue("Conference");
                txtSpecialite.setText(conf.getTheme());
                txtIntervenants.setText(String.join("\n", conf.getIntervenants()));
                mettreAJourChampsSpecialises();
            } else if (evt instanceof Concert) {
                Concert conc = (Concert) evt;
                cmbTypeEvenement.setValue("Concert");
                txtSpecialite.setText(conc.getArtiste());
                txtGenreMusical.setText(conc.getGenreMusical());
                mettreAJourChampsSpecialises();
            }

            // Afficher les détails
            afficherDetailsEvenement(evt);
        }
    }

    private void afficherDetailsEvenement(Evenement evt) {
        StringBuilder details = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        details.append("=== DÉTAILS DE L'ÉVÉNEMENT ===\n");
        details.append("ID: ").append(evt.getId()).append("\n");
        details.append("Nom: ").append(evt.getNom()).append("\n");
        details.append("Date: ").append(evt.getDate().format(formatter)).append("\n");
        details.append("Lieu: ").append(evt.getLieu()).append("\n");
        details.append("Capacité: ").append(evt.getParticipants().size()).append("/").append(evt.getCapaciteMax()).append("\n");
        details.append("Statut: ").append(evt.isAnnule() ? "ANNULÉ" : "ACTIF").append("\n");

        if (evt instanceof Conference) {
            Conference conf = (Conference) evt;
            details.append("Type: Conférence\n");
            details.append("Thème: ").append(conf.getTheme()).append("\n");
            details.append("Intervenants: ").append(String.join(", ", conf.getIntervenants())).append("\n");
        } else if (evt instanceof Concert) {
            Concert conc = (Concert) evt;
            details.append("Type: Concert\n");
            details.append("Artiste: ").append(conc.getArtiste()).append("\n");
            details.append("Genre: ").append(conc.getGenreMusical()).append("\n");
        }

        details.append("\n=== PARTICIPANTS INSCRITS ===\n");
        if (evt.getParticipants().isEmpty()) {
            details.append("Aucun participant inscrit\n");
        } else {
            for (int i = 0; i < evt.getParticipants().size(); i++) {
                details.append((i + 1)).append(". ").append(evt.getParticipants().get(i)).append("\n");
            }
        }

        txtDetailsEvenement.setText(details.toString());
    }

    private boolean validerSaisie() {
        StringBuilder erreurs = new StringBuilder();

        if (txtNomEvenement.getText().trim().isEmpty()) {
            erreurs.append("• Le nom de l'événement est obligatoire\n");
        }

        if (txtLieu.getText().trim().isEmpty()) {
            erreurs.append("• Le lieu est obligatoire\n");
        }

        if (datePicker.getValue() == null) {
            erreurs.append("• La date est obligatoire\n");
        }

        if (cmbHeures.getValue() == null || cmbMinutes.getValue() == null) {
            erreurs.append("• L'heure est obligatoire\n");
        }

        if (txtCapacite.getText().trim().isEmpty()) {
            erreurs.append("• La capacité est obligatoire\n");
        } else {
            try {
                int capacite = Integer.parseInt(txtCapacite.getText());
                if (capacite <= 0) {
                    erreurs.append("• La capacité doit être positive\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("• La capacité doit être un nombre valide\n");
            }
        }

        if (cmbTypeEvenement.getValue() == null) {
            erreurs.append("• Le type d'événement est obligatoire\n");
        }

        if (txtSpecialite.getText().trim().isEmpty()) {
            erreurs.append("• Le thème/artiste est obligatoire\n");
        }

        if (cmbTypeEvenement.getValue() != null) {
            if (cmbTypeEvenement.getValue().equals("Concert") && txtGenreMusical.getText().trim().isEmpty()) {
                erreurs.append("• Le genre musical est obligatoire pour un concert\n");
            }
        }

        if (erreurs.length() > 0) {
            afficherMessage("❌ Erreurs de validation :\n" + erreurs.toString(), Color.RED);
            return false;
        }

        return true;
    }

    private void creerEvenement() {
        if (!validerSaisie()) return;

        String id = "EVT" + (evenements.size() + 1);
        String nom = txtNomEvenement.getText().trim();
        String lieu = txtLieu.getText().trim();
        LocalDateTime date = datePicker.getValue().atTime(
                Integer.parseInt(cmbHeures.getValue()),
                Integer.parseInt(cmbMinutes.getValue())
        );
        int capacite = Integer.parseInt(txtCapacite.getText());
        String type = cmbTypeEvenement.getValue();
        String specialite = txtSpecialite.getText().trim();

        try {
            Evenement nouvelEvenement;
            if (type.equals("Conference")) {
                List<String> intervenants = new ArrayList<>();
                if (!txtIntervenants.getText().trim().isEmpty()) {
                    intervenants = Arrays.asList(txtIntervenants.getText().split("\n"));
                }
                nouvelEvenement = new ConferenceImpl(id, nom, date, lieu, capacite, specialite, new ArrayList<>(intervenants));
            } else {
                String genreMusical = txtGenreMusical.getText().trim();
                nouvelEvenement = new ConcertImpl(id, nom, date, lieu, capacite, specialite, genreMusical);
            }

            evenements.put(id, nouvelEvenement);

            // Ajouter à la liste des événements organisés
            organisateurConnecte.getEvenementsOrganises().add(nouvelEvenement);

            // Notification
            String message = "Nouvel événement créé: " + nom + " le " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));
            interfaceNotifications.ajouterNotification(message, nouvelEvenement.getParticipants());

            afficherMessage("✅ Événement créé avec succès : " + nom, Color.GREEN);
            actualiserListeEvenements();
            effacerChamps();

        } catch (Exception e) {
            afficherMessage("❌ Erreur lors de la création : " + e.getMessage(), Color.RED);
        }
    }

    private void modifierEvenement() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherMessage("⚠️ Veuillez sélectionner un événement à modifier", Color.ORANGE);
            return;
        }

        if (!validerSaisie()) return;

        String id = selection.split(" - ")[0].replace("[ANNULÉ] ", "");
        Evenement evt = evenements.get(id);

        if (evt != null && !evt.isAnnule()) {
            // Sauvegarder les anciennes valeurs pour notification
            String ancienNom = evt.getNom();
            LocalDateTime ancienneDate = evt.getDate();

            evt.setNom(txtNomEvenement.getText().trim());
            evt.setLieu(txtLieu.getText().trim());
            evt.setDate(datePicker.getValue().atTime(
                    Integer.parseInt(cmbHeures.getValue()),
                    Integer.parseInt(cmbMinutes.getValue())
            ));
            evt.setCapaciteMax(Integer.parseInt(txtCapacite.getText()));

            if (evt instanceof Conference && cmbTypeEvenement.getValue().equals("Conference")) {
                Conference conf = (Conference) evt;
                conf.setTheme(txtSpecialite.getText().trim());
                if (!txtIntervenants.getText().trim().isEmpty()) {
                    conf.setIntervenants(Arrays.asList(txtIntervenants.getText().split("\n")));
                }
            } else if (evt instanceof Concert && cmbTypeEvenement.getValue().equals("Concert")) {
                Concert conc = (Concert) evt;
                conc.setArtiste(txtSpecialite.getText().trim());
                conc.setGenreMusical(txtGenreMusical.getText().trim());
            }

            // Notification des modifications
            String message = String.format("Événement modifié: %s\nNouvelles informations: %s le %s",
                    ancienNom,
                    evt.getNom(),
                    evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
            interfaceNotifications.ajouterNotification(message, evt.getParticipants());

            afficherMessage("✅ Événement modifié avec succès", Color.GREEN);
            actualiserListeEvenements();
            afficherDetailsEvenement(evt);
        } else {
            afficherMessage("❌ Impossible de modifier un événement annulé", Color.RED);
        }
    }

    private void annulerEvenement() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherMessage("⚠️ Veuillez sélectionner un événement à annuler", Color.ORANGE);
            return;
        }

        String id = selection.split(" - ")[0].replace("[ANNULÉ] ", "");
        Evenement evt = evenements.get(id);

        if (evt != null && !evt.isAnnule()) {
            try {
                evt.annuler();

                // Notification d'annulation
                String message = "ANNULATION: L'événement '" + evt.getNom() + "' prévu le " +
                        evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + " a été annulé.";
                interfaceNotifications.ajouterNotification(message, evt.getParticipants());

                afficherMessage("ℹ️ Événement annulé : " + evt.getNom(), Color.BLUE);
                actualiserListeEvenements();
                afficherDetailsEvenement(evt);
                effacerChamps();

            } catch (EvenementDejaExistantException e) {
                afficherMessage("ℹ️ " + e.getMessage(), Color.BLUE);
                actualiserListeEvenements();
            }
        } else {
            afficherMessage("❌ Événement déjà annulé ou introuvable", Color.RED);
        }
    }

    private void ajouterParticipant() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        String email = txtNouveauParticipant.getText().trim();

        if (selection == null) {
            afficherMessage("⚠️ Veuillez sélectionner un événement", Color.ORANGE);
            return;
        }

        if (email.isEmpty()) {
            afficherMessage("⚠️ Veuillez saisir l'email du participant", Color.ORANGE);
            return;
        }

        String id = selection.split(" - ")[0].replace("[ANNULÉ] ", "");
        Evenement evt = evenements.get(id);

        if (evt != null) {
            try {
                evt.ajouterparticipant(email);

                // Notification au nouveau participant
                String message = "Inscription confirmée pour l'événement: " + evt.getNom() +
                        " le " + evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));
                interfaceNotifications.ajouterNotificationIndividuelle(message, email);

                afficherMessage("✅ Participant ajouté avec succès", Color.GREEN);
                txtNouveauParticipant.clear();
                actualiserListeEvenements();
                afficherDetailsEvenement(evt);

            } catch (CapaciteMaxAtteinteException e) {
                afficherMessage("❌ " + e.getMessage(), Color.RED);
            }
        }
    }
    private void effacerChamps() {
        txtNomEvenement.clear();
        txtLieu.clear();
        datePicker.setValue(null);
        cmbHeures.setValue(null);
        cmbMinutes.setValue(null);
        txtCapacite.clear();
        cmbTypeEvenement.setValue(null);
        txtSpecialite.clear();
        txtGenreMusical.clear();
        txtIntervenants.clear();
        txtNouveauParticipant.clear();
    }

    private void afficherMessage(String message, Color couleur) {
        lblStatut.setText(message);
        lblStatut.setTextFill(couleur);
    }

    // Classes d'implémentation concrètes pour les événements
    private static class ConferenceImpl extends Conference {
        public ConferenceImpl(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                              String theme, ArrayList<String> intervenants) {
            super(id, nom, date, lieu, capaciteMax, theme, intervenants);
        }
    }

    private static class ConcertImpl extends Concert {
        public ConcertImpl(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                           String artiste, String genreMusical) {
            super(id, nom, date, lieu, capaciteMax, artiste, genreMusical);
        }
    }
}*/
}
