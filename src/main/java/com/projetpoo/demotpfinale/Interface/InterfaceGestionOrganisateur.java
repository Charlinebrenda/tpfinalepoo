package com.projetpoo.demotpfinale.Interface;

import com.projetpoo.demotpfinale.Exception.CapaciteMaxAtteinteException;
import com.projetpoo.demotpfinale.modele.*;
import com.projetpoo.demotpfinale.Serialisation.EvenementSerializer;
import com.projetpoo.demotpfinale.Serialisation.EvenementDeserializer;
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
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InterfaceGestionOrganisateur extends VBox {
    private TextField txtNomEvenement, txtLieu, txtCapacite, txtSpecialite, txtGenreMusical, txtNouveauParticipant;
    private DatePicker datePicker;
    private ComboBox<String> cmbHeures, cmbMinutes, cmbTypeEvenement;
    private TextArea txtIntervenants, txtDetailsEvenement;
    private ListView<String> listEvenements;
    private Label lblStatut;
    private Map<String, Evenement> evenements;
    private GestionEvenement gestionEvenement;
    private Organisateur organisateurConnecte;
    private InterfaceNotifications interfaceNotifications;
    private final String JSON_FILE = "C:/Users/Tchaweu/Documents/demotpfinale/events.json";
    private final EvenementSerializer serializer;
    private final EvenementDeserializer deserializer;

    public InterfaceGestionOrganisateur(Organisateur organisateur) throws IOException {
        this.organisateurConnecte = organisateur;
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f8f9fa;");

        evenements = new HashMap<>();
        gestionEvenement = GestionEvenement.getInstance(evenements);
        interfaceNotifications = new InterfaceNotifications();
        serializer = new EvenementSerializer(JSON_FILE);
        deserializer = new EvenementDeserializer(JSON_FILE);

        construireInterface();

        Platform.runLater(() -> {
            loadFromFile();
            actualiserListeEvenements();
        });
    }

    public void afficherInterface(Organisateur organisateur, Stage stagePrincipal) {
        Scene scene = new Scene(this, 900, 700);
        stagePrincipal.setTitle("Gestion des Événements");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void construireInterface() {
        VBox headerBox = new VBox(5);
        Label titre = new Label("Gestion des Événements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);
        Label orgLabel = new Label("Organisateur: " + organisateurConnecte.getNom());
        orgLabel.setFont(Font.font("Arial", 14));
        headerBox.getChildren().addAll(titre, orgLabel);
        headerBox.setAlignment(Pos.CENTER);

        VBox sectionGestion = new VBox(10);
        sectionGestion.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nom:"), 0, 0);
        txtNomEvenement = new TextField();
        grid.add(txtNomEvenement, 1, 0);

        grid.add(new Label("Lieu:"), 0, 1);
        txtLieu = new TextField();
        grid.add(txtLieu, 1, 1);

        grid.add(new Label("Date:"), 0, 2);
        HBox dateBox = new HBox(5);
        datePicker = new DatePicker();
        cmbHeures = new ComboBox<>();
        for (int i = 0; i < 24; i++) cmbHeures.getItems().add(String.format("%02d", i));
        cmbMinutes = new ComboBox<>();
        for (int i = 0; i < 60; i += 15) cmbMinutes.getItems().add(String.format("%02d", i));
        dateBox.getChildren().addAll(datePicker, cmbHeures, cmbMinutes);
        grid.add(dateBox, 1, 2);

        grid.add(new Label("Capacité:"), 0, 3);
        txtCapacite = new TextField();
        grid.add(txtCapacite, 1, 3);

        grid.add(new Label("Type:"), 0, 4);
        cmbTypeEvenement = new ComboBox<>();
        cmbTypeEvenement.getItems().addAll("Conference", "Concert");
        cmbTypeEvenement.setOnAction(e -> toggleFields());
        grid.add(cmbTypeEvenement, 1, 4);

        grid.add(new Label("Thème/Artiste:"), 0, 5);
        txtSpecialite = new TextField();
        grid.add(txtSpecialite, 1, 5);

        grid.add(new Label("Genre musical:"), 0, 6);
        txtGenreMusical = new TextField();
        txtGenreMusical.setVisible(false);
        grid.add(txtGenreMusical, 1, 6);

        grid.add(new Label("Intervenants:"), 0, 7);
        txtIntervenants = new TextArea();
        txtIntervenants.setPrefHeight(60);
        txtIntervenants.setVisible(false);
        grid.add(txtIntervenants, 1, 7);

        HBox buttons = new HBox(10);
        Button btnCreer = new Button("Créer");
        btnCreer.setOnAction(e -> creerEvenement());
        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(e -> modifierEvenement());
        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(e -> annulerEvenement());
        Button btnActualiser = new Button("Actualiser");
        btnActualiser.setOnAction(e -> {
            loadFromFile();
            actualiserListeEvenements();
        });
        buttons.getChildren().addAll(btnCreer, btnModifier, btnAnnuler, btnActualiser);
        sectionGestion.getChildren().addAll(grid, buttons);

        HBox mainSection = new HBox(15);
        VBox listSection = new VBox(10);
        listEvenements = new ListView<>();
        listEvenements.setPrefHeight(300);
        listEvenements.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) chargerDetails(newVal);
        });
        listSection.getChildren().addAll(new Label("Événements"), listEvenements);

        VBox detailsSection = new VBox(10);
        txtDetailsEvenement = new TextArea();
        txtDetailsEvenement.setEditable(false);
        txtNouveauParticipant = new TextField();
        txtNouveauParticipant.setPromptText("Email du participant");
        Button btnAddParticipant = new Button("Ajouter Participant");
        btnAddParticipant.setOnAction(e -> ajouterParticipant());
        detailsSection.getChildren().addAll(new Label("Détails"), txtDetailsEvenement, txtNouveauParticipant, btnAddParticipant);

        mainSection.getChildren().addAll(listSection, detailsSection);

        lblStatut = new Label("Interface initialisée");
        lblStatut.setTextFill(Color.BLUE);
        this.getChildren().addAll(headerBox, sectionGestion, mainSection, lblStatut);
    }

    private void toggleFields() {
        String type = cmbTypeEvenement.getValue();
        txtGenreMusical.setVisible("Concert".equals(type));
        txtIntervenants.setVisible("Conference".equals(type));
        txtSpecialite.setPromptText("Concert".equals(type) ? "Artiste" : "Thème");
    }

    private void actualiserListeEvenements() {
        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            System.out.println("Actualizing list with " + evenements.size() + " events");

            for (Map.Entry<String, Evenement> entry : evenements.entrySet()) {
                Evenement evt = entry.getValue();
                if (evt != null) {
                    String displayText = String.format("%s - %s (%s)",
                            entry.getKey(),
                            evt.getNom(),
                            evt.getDate() != null ? evt.getDate().format(fmt) : "Date non définie");
                    items.add(displayText);
                    System.out.println("Added to list: " + displayText);
                }
            }

            listEvenements.setItems(items);
            listEvenements.refresh();

            afficherMessage("Liste actualisée - " + items.size() + " événements", Color.BLUE);
        });
    }

    private void chargerDetails(String selection) {
        if (selection == null || selection.trim().isEmpty()) return;

        String id = selection.split(" - ")[0];
        Evenement evt = evenements.get(id);

        if (evt != null) {
            txtNomEvenement.setText(evt.getNom());
            txtLieu.setText(evt.getLieu());

            if (evt.getDate() != null) {
                datePicker.setValue(evt.getDate().toLocalDate());
                cmbHeures.setValue(String.format("%02d", evt.getDate().getHour()));
                cmbMinutes.setValue(String.format("%02d", evt.getDate().getMinute()));
            }

            txtCapacite.setText(String.valueOf(evt.getCapaciteMax()));

            if (evt instanceof Conference conf) {
                cmbTypeEvenement.setValue("Conference");
                txtSpecialite.setText(conf.getTheme());
                if (conf.getIntervenants() != null) {
                    txtIntervenants.setText(String.join("\n", conf.getIntervenants()));
                }
            } else if (evt instanceof Concert conc) {
                cmbTypeEvenement.setValue("Concert");
                txtSpecialite.setText(conc.getArtiste());
                txtGenreMusical.setText(conc.getGenreMusical());
            }

            toggleFields();

            String details = String.format("ID: %s\nNom: %s\nDate: %s\nLieu: %s\nCapacité: %d/%d\nParticipants: %s",
                    evt.getId(),
                    evt.getNom(),
                    evt.getDate() != null ? evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Non définie",
                    evt.getLieu(),
                    evt.getParticipants() != null ? evt.getParticipants().size() : 0,
                    evt.getCapaciteMax(),
                    evt.getParticipants() != null ? evt.getParticipants().toString() : "[]");
            txtDetailsEvenement.setText(details);
        }
    }

    private boolean validerSaisie() {
        if (txtNomEvenement.getText() == null || txtNomEvenement.getText().isBlank() ||
                txtLieu.getText() == null || txtLieu.getText().isBlank() ||
                datePicker.getValue() == null ||
                cmbHeures.getValue() == null || cmbMinutes.getValue() == null ||
                txtCapacite.getText() == null || txtCapacite.getText().isBlank() ||
                cmbTypeEvenement.getValue() == null ||
                txtSpecialite.getText() == null || txtSpecialite.getText().isBlank()) {
            afficherMessage("Tous les champs obligatoires doivent être remplis", Color.RED);
            return false;
        }

        try {
            int capacite = Integer.parseInt(txtCapacite.getText());
            if (capacite <= 0) {
                afficherMessage("Capacité doit être positive", Color.RED);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherMessage("Capacité doit être un nombre", Color.RED);
            return false;
        }

        if ("Concert".equals(cmbTypeEvenement.getValue()) &&
                (txtGenreMusical.getText() == null || txtGenreMusical.getText().isBlank())) {
            afficherMessage("Genre musical requis pour un concert", Color.RED);
            return false;
        }

        return true;
    }


    private void ajouterEvenementAvecSauvegarde(String id, Evenement evenement) throws IOException {
        try {
            System.out.println("Adding event: ID=" + id + ", Name=" + evenement.getNom());
            Map<String, Evenement> nouveauxEvenements = new HashMap<>();
            nouveauxEvenements.put(id, evenement);
            evenements.put(id, evenement); // Mettre à jour la map locale
            serializer.serialize(nouveauxEvenements);
            System.out.println("Event added and serialized successfully");
        } catch (IOException e) {
            System.err.println("Failed to add event: " + e.getMessage());
            throw e;
        }
    }
    private void creerEvenement() {
        if (!validerSaisie()) return;

        try {
            String id = "EVT" + System.currentTimeMillis();
            String nom = txtNomEvenement.getText().trim();
            LocalDateTime date = datePicker.getValue().atTime(
                    Integer.parseInt(cmbHeures.getValue()), Integer.parseInt(cmbMinutes.getValue()));
            String lieu = txtLieu.getText().trim();
            int capacite = Integer.parseInt(txtCapacite.getText());
            String type = cmbTypeEvenement.getValue();

            Evenement evt;
            if ("Conference".equals(type)) {
                List<String> intervenants = (txtIntervenants.getText() == null || txtIntervenants.getText().isBlank()) ?
                        new ArrayList<>() : Arrays.asList(txtIntervenants.getText().split("\n"));
                evt = new ConferenceImpl(id, nom, date, lieu, capacite, txtSpecialite.getText().trim(), intervenants);
            } else {
                evt = new ConcertImpl(id, nom, date, lieu, capacite, txtSpecialite.getText().trim(), txtGenreMusical.getText().trim());
            }

            System.out.println("Création de l'événement: " + id + " - " + nom);

            // Ajouter et sauvegarder
            ajouterEvenementAvecSauvegarde(id, evt);

            // Mettre à jour les autres composants
            gestionEvenement.ajouterEvenement(evt);
            organisateurConnecte.getEvenementsOrganises().add(evt);

            interfaceNotifications.ajouterNotificationIndividuelle("Événement créé: " + nom, organisateurConnecte.getEmail());
            afficherMessage("Événement créé: " + nom, Color.GREEN);
            actualiserListeEvenements();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            afficherMessage("Erreur lors de la création: " + e.getMessage(), Color.RED);
        }
    }

    private void modifierEvenement() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherMessage("Sélectionnez un événement", Color.RED);
            return;
        }
        if (!validerSaisie()) return;

        String id = selection.split(" - ")[0];
        Evenement evt = evenements.get(id);
        if (evt != null) {
            try {
                evt.setNom(txtNomEvenement.getText().trim());
                evt.setLieu(txtLieu.getText().trim());
                evt.setDate(datePicker.getValue().atTime(
                        Integer.parseInt(cmbHeures.getValue()), Integer.parseInt(cmbMinutes.getValue())));
                evt.setCapaciteMax(Integer.parseInt(txtCapacite.getText()));

                if (evt instanceof Conference conf && "Conference".equals(cmbTypeEvenement.getValue())) {
                    conf.setTheme(txtSpecialite.getText().trim());
                    conf.setIntervenants((txtIntervenants.getText() == null || txtIntervenants.getText().isBlank()) ?
                            new ArrayList<>() : Arrays.asList(txtIntervenants.getText().split("\n")));
                } else if (evt instanceof Concert conc && "Concert".equals(cmbTypeEvenement.getValue())) {
                    conc.setArtiste(txtSpecialite.getText().trim());
                    conc.setGenreMusical(txtGenreMusical.getText().trim());
                }

                // Sauvegarder les modifications
                saveToFileRobuste();

                interfaceNotifications.ajouterNotificationIndividuelle("Événement modifié: " + evt.getNom(), organisateurConnecte.getEmail());
                afficherMessage("Événement modifié", Color.GREEN);
                actualiserListeEvenements();

            } catch (Exception e) {
                e.printStackTrace();
                afficherMessage("Erreur lors de la modification: " + e.getMessage(), Color.RED);
            }
        }
    }

    private void annulerEvenement() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherMessage("Sélectionnez un événement", Color.RED);
            return;
        }

        String id = selection.split(" - ")[0];
        Evenement evt = evenements.get(id);
        if (evt != null) {
            try {
                evenements.remove(id);
                gestionEvenement.supprimerEvenement(id);
                organisateurConnecte.getEvenementsOrganises().remove(evt);

                // Sauvegarder la suppression
                saveToFileRobuste();

                interfaceNotifications.ajouterNotificationIndividuelle("Événement annulé: " + evt.getNom(), organisateurConnecte.getEmail());
                afficherMessage("Événement annulé", Color.GREEN);
                actualiserListeEvenements();
                txtDetailsEvenement.clear();
                clearFields();

            } catch (Exception e) {
                e.printStackTrace();
                afficherMessage("Erreur lors de l'annulation: " + e.getMessage(), Color.RED);
            }
        }
    }

    private void ajouterParticipant() {
        String selection = listEvenements.getSelectionModel().getSelectedItem();
        String email = txtNouveauParticipant.getText() != null ? txtNouveauParticipant.getText().trim() : "";

        if (selection == null || email.isBlank()) {
            afficherMessage("Sélectionnez un événement et entrez un email", Color.RED);
            return;
        }

        String id = selection.split(" - ")[0];
        Evenement evt = evenements.get(id);
        if (evt != null) {
            try {
                evt.ajouterparticipant("Participant (" + email + ")");

                // Sauvegarder l'ajout de participant
                saveToFileRobuste();

                interfaceNotifications.ajouterNotificationIndividuelle("Inscrit à: " + evt.getNom(), email);
                afficherMessage("Participant ajouté", Color.GREEN);
                txtNouveauParticipant.clear();
                actualiserListeEvenements();
                chargerDetails(selection);

            } catch (CapaciteMaxAtteinteException e) {
                afficherMessage("Capacité maximale atteinte", Color.RED);
            } catch (Exception e) {
                e.printStackTrace();
                afficherMessage("Erreur lors de l'ajout du participant: " + e.getMessage(), Color.RED);
            }
        }
    }

    private void clearFields() {
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

    private void afficherMessage(String message, Color color) {
        Platform.runLater(() -> {
            lblStatut.setText(message);
            lblStatut.setTextFill(color);
            System.out.println("Status: " + message);
        });
    }

    private void saveToFileRobuste() throws IOException {
        try {
            System.out.println("Saving " + evenements.size() + " events");
            serializer.serialize(new HashMap<>(evenements));
            System.out.println("Events saved successfully");
        } catch (IOException e) {
            System.err.println("Failed to save events: " + e.getMessage());
            throw e;
        }
    }
    public void saveToFile() {
        try {
            saveToFileRobuste();
        } catch (IOException e) {
            e.printStackTrace();
            afficherMessage("Erreur de sauvegarde: " + e.getMessage(), Color.RED);
        }
    }

    private void loadFromFile() {
        try {
            System.out.println("Loading events from file...");
            Map<String, Evenement> loadedEvents = deserializer.deserialize();
            evenements.clear();
            evenements.putAll(loadedEvents);
            gestionEvenement = GestionEvenement.getInstance(evenements);
            organisateurConnecte.getEvenementsOrganises().clear();
            organisateurConnecte.getEvenementsOrganises().addAll(evenements.values());
            System.out.println("Loaded " + evenements.size() + " events");
            afficherMessage("Chargé " + evenements.size() + " événements", Color.GREEN);
        } catch (IOException e) {
            System.err.println("Failed to load events: " + e.getMessage());
            e.printStackTrace();
            afficherMessage("Erreur de chargement: " + e.getMessage(), Color.RED);
        }
    }

    // ... (reste des méthodes inchangées : creerEvenement, modifierEvenement, annulerEvenement, ajouterParticipant)
}