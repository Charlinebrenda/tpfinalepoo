package com.projetpoo.demotpfinale.Interface;

import com.projetpoo.demotpfinale.modele.Evenement;
import com.projetpoo.demotpfinale.modele.Organisateur;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterfaceNotifications {

    private Stage stageNotifications;
    private ListView<String> listeNotifications;
    private List<Notification> notifications;
    private Organisateur organisateurCourant;
    private Evenement evt;

    public InterfaceNotifications() {
        this.notifications = new ArrayList<>();
        initialiserNotificationsExemple();
    }

    public void afficherInterface(Organisateur organisateur, Stage stagePrincipal) {
        this.organisateurCourant = organisateur;

        if (stageNotifications == null) {
            stageNotifications = new Stage();
            creerInterface();
        }

        // Actualiser les notifications
        actualiserNotifications();
        stageNotifications.show();
        stageNotifications.toFront();
    }

    private void creerInterface() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd 0%, #bbdefb 100%);");

        // En-tête
        VBox header = creerEntete();
        root.setTop(header);

        // Centre - Liste des notifications
        VBox centre = creerZoneCentrale();
        root.setCenter(centre);

        // Bas - Boutons d'action
        HBox boutonsAction = creerBoutonsAction();
        root.setBottom(boutonsAction);

        Scene scene = new Scene(root, 700, 600);
        stageNotifications.setTitle("🔔 Centre de Notifications");
        stageNotifications.setScene(scene);
    }

    private VBox creerEntete() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: rgba(255,255,255,0.9); " +
                "-fx-background-radius: 0 0 15 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titre = new Label("🔔 Centre de Notifications");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);

        Label sousTitre = new Label("Organisateur: " +
                (organisateurCourant != null ? organisateurCourant.getNom() : "Non défini"));
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.GRAY);

        // Compteur de notifications
        Label compteur = new Label("📊 " + notifications.size() + " notification(s) au total");
        compteur.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        compteur.setTextFill(Color.DARKGREEN);

        header.getChildren().addAll(titre, sousTitre, compteur);
        return header;
    }

    private VBox creerZoneCentrale() {
        VBox centre = new VBox(15);
        centre.setPadding(new Insets(20));

        Label lblTitre = new Label("📋 Vos Notifications");
        lblTitre.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitre.setTextFill(Color.DARKBLUE);

        // Liste des notifications avec style personnalisé
        listeNotifications = new ListView<>();
        listeNotifications.setPrefHeight(350);
        listeNotifications.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Configuration de la cellule personnalisée
        listeNotifications.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; " +
                            "-fx-border-width: 0 0 1 0; -fx-font-size: 13px;");

                    // Colorer selon le type de notification
                    if (item.startsWith("🚨")) {
                        setStyle(getStyle() + "-fx-background-color: #ffebee; -fx-text-fill: #c62828;");
                    } else if (item.startsWith("⚠️")) {
                        setStyle(getStyle() + "-fx-background-color: #fff3e0; -fx-text-fill: #f57c00;");
                    } else if (item.startsWith("✅")) {
                        setStyle(getStyle() + "-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32;");
                    } else if (item.startsWith("📅")) {
                        setStyle(getStyle() + "-fx-background-color: #e1f5fe; -fx-text-fill: #0277bd;");
                    }
                }
            }
        });

        centre.getChildren().addAll(lblTitre, listeNotifications);
        return centre;
    }

    private HBox creerBoutonsAction() {
        HBox boutonsAction = new HBox(15);
        boutonsAction.setPadding(new Insets(20));
        boutonsAction.setStyle("-fx-background-color: rgba(255,255,255,0.8); " +
                "-fx-background-radius: 15 15 0 0;");

        Button btnActualiser = new Button("🔄 Actualiser");
        btnActualiser.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8;");
        btnActualiser.setOnAction(e -> {
            actualiserNotifications();
            afficherInformation("Actualisation", "Les notifications ont été actualisées !");
        });

        Button btnMarquerToutLu = new Button("✅ Tout marquer comme lu");
        btnMarquerToutLu.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8;");
        btnMarquerToutLu.setOnAction(e -> {
            marquerToutCommeLu();
            afficherInformation("Notifications", "Toutes les notifications ont été marquées comme lues !");
        });

        Button btnSupprimerTout = new Button("🗑️ Tout supprimer");
        btnSupprimerTout.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8;");
        btnSupprimerTout.setOnAction(e -> {
            if (confirmerSuppression()) {
                supprimerToutesNotifications();
                afficherInformation("Notifications", "Toutes les notifications ont été supprimées !");
            }
        });

        Button btnFermer = new Button("❌ Fermer");
        btnFermer.setStyle("-fx-background-color: #757575; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8;");
        btnFermer.setOnAction(e -> stageNotifications.close());

        // Espacement flexible
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        boutonsAction.getChildren().addAll(btnActualiser, btnMarquerToutLu, btnSupprimerTout, spacer, btnFermer);
        return boutonsAction;
    }

    private void actualiserNotifications() {
        // Ajouter de nouvelles notifications basées sur l'état des événements
        verifierEvenementsProches();

        // Mettre à jour la liste affichée
        listeNotifications.getItems().clear();
        for (Notification notif : notifications) {
            String prefixe = notif.isLue() ? "[LU] " : "[NOUVEAU] ";
            listeNotifications.getItems().add(prefixe + notif.getContenu());
        }
    }

    private void verifierEvenementsProches() {
        if (organisateurCourant != null && organisateurCourant.getEvenementsOrganises() != null) {
            LocalDateTime maintenant = LocalDateTime.now();

            for (Evenement evt : organisateurCourant.getEvenementsOrganises()) {
                // Vérifier les événements dans les 7 prochains jours
                if (evt.getDate() != null &&
                        evt.getDate().isAfter(maintenant) &&
                        evt.getDate().isBefore(maintenant.plusDays(7))) {

                    String message = "📅 Événement \"" + evt.getNom() + "\" prévu le " +
                            evt.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));

                    ajouterNotification(TypeNotification.INFO, message);
                }

                // Vérifier les événements annulés
                if (evt.isAnnule()) {
                    String message = "🚨 L'événement \"" + evt.getNom() + "\" a été annulé";
                    ajouterNotification(TypeNotification.URGENT, message);
                }

                // Vérifier les événements sans participants
                if (evt.getParticipants() == null || evt.getParticipants().isEmpty()) {
                    String message = "⚠️ L'événement \"" + evt.getNom() + "\" n'a aucun participant inscrit";
                    ajouterNotification(TypeNotification.ATTENTION, message);
                }

                // Vérifier les événements proches de la capacité max
                if (evt.getParticipants() != null && evt.getCapaciteMax() > 0) {
                    double pourcentage = (double) evt.getParticipants().size() / evt.getCapaciteMax() * 100;
                    if (pourcentage >= 90) {
                        String message = "⚠️ L'événement \"" + evt.getNom() + "\" est presque complet (" +
                                evt.getParticipants().size() + "/" + evt.getCapaciteMax() + ")";
                        ajouterNotification(TypeNotification.ATTENTION, message);
                    }
                }
            }
        }
    }

    private void initialiserNotificationsExemple() {
        LocalDateTime maintenant = LocalDateTime.now();

        ajouterNotification(TypeNotification.SUCCES,
                "✅ Votre compte organisateur a été activé avec succès");

        ajouterNotification(TypeNotification.INFO,
                "📅 N'oubliez pas de vérifier vos événements à venir");

        ajouterNotification(TypeNotification.ATTENTION,
                "⚠️ Certains événements nécessitent votre attention");

        ajouterNotification(TypeNotification.URGENT,
                "🚨 Rappel: Validation requise pour l'événement du weekend");
    }

    private void ajouterNotification(TypeNotification type, String contenu) {
        // Éviter les doublons
        boolean existe = notifications.stream()
                .anyMatch(n -> n.getContenu().equals(contenu));

        if (!existe) {
            Notification notification = new Notification(type, contenu, LocalDateTime.now());
            notifications.add(0, notification); // Ajouter en première position
        }
    }
    public void ajouterNotification(String message, List<String> destinataires) {
        for (String destinataire : destinataires) {
            String contenu = message + " (Destinataire: " + destinataire + ")";
            ajouterNotification(TypeNotification.SUCCES, contenu);
        }
        if (destinataires.isEmpty()) {
            ajouterNotification(TypeNotification.SUCCES, message + " (Aucun participant)");
        }
    }

    public void ajouterNotificationIndividuelle(String message, String destinataire) {
        String contenu = message + " (Destinataire: " + destinataire + ")";
        ajouterNotification(TypeNotification.SUCCES, contenu);
    }

    private void marquerToutCommeLu() {
        for (Notification notif : notifications) {
            notif.setLue(true);
        }
        actualiserNotifications();
    }

    private void supprimerToutesNotifications() {
        notifications.clear();
        actualiserNotifications();
    }

    private boolean confirmerSuppression() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer toutes les notifications");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer toutes les notifications ?\nCette action est irréversible.");

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void afficherInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Classes internes pour la gestion des notifications
    private static class Notification {
        private TypeNotification type;
        private String contenu;
        private LocalDateTime dateCreation;
        private boolean lue;

        public Notification(TypeNotification type, String contenu, LocalDateTime dateCreation) {
            this.type = type;
            this.contenu = contenu;
            this.dateCreation = dateCreation;
            this.lue = false;
        }

        // Getters et setters
        public TypeNotification getType() { return type; }
        public String getContenu() { return contenu; }
        public LocalDateTime getDateCreation() { return dateCreation; }
        public boolean isLue() { return lue; }
        public void setLue(boolean lue) { this.lue = lue; }
    }

    private enum TypeNotification {
        INFO("📅"),
        ATTENTION("⚠️"),
        URGENT("🚨"),
        SUCCES("✅");

        private final String icone;

        TypeNotification(String icone) {
            this.icone = icone;
        }

        public String getIcone() { return icone; }
    }
}
