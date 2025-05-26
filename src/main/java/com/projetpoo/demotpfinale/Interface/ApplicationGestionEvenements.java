package com.projetpoo.demotpfinale.Interface;
import com.projetpoo.demotpfinale.Interface.InterfaceGestionOrganisateur;
import com.projetpoo.demotpfinale.Interface.InterfaceNotifications;
import com.projetpoo.demotpfinale.modele.Organisateur;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;

public class ApplicationGestionEvenements extends Application {
    private Stage stagePrincipal;
    private InterfaceGestionOrganisateur interfaceOrganisateur;
    private InterfaceNotifications interfaceNotifications;

    @Override
    public void start(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;

        // Créer un organisateur de test
        Organisateur organisateur = new Organisateur(
                "ORG001",
                "Jean Dupont",
                "jean.dupont@organisation.fr",
                new ArrayList<>()
        );

        // Créer l'interface de connexion/sélection
        creerInterfaceConnexion(organisateur);
    }

    private void creerInterfaceConnexion(Organisateur organisateur) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Titre de l'application
        Label titre = new Label("🎭 Système de Gestion d'Événements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titre.setTextFill(Color.WHITE);
        titre.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        // Informations de l'organisateur connecté
        VBox infoOrganisateur = new VBox(10);
        infoOrganisateur.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 20; -fx-background-radius: 10;");

        Label lblBienvenue = new Label("👋 Bienvenue, " + organisateur.getNom());
        lblBienvenue.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblBienvenue.setTextFill(Color.DARKBLUE);

        Label lblEmail = new Label("📧 " + organisateur.getEmail());
        lblEmail.setFont(Font.font("Arial", 14));
        lblEmail.setTextFill(Color.GRAY);

        Label lblId = new Label("🆔 ID: " + organisateur.getId());
        lblId.setFont(Font.font("Arial", 12));
        lblId.setTextFill(Color.GRAY);

        infoOrganisateur.getChildren().addAll(lblBienvenue, lblEmail, lblId);

        // Boutons de navigation
        VBox boutonsNav = new VBox(15);

        Button btnGestionEvenements = new Button("🎪 Gestion des Événements");
        btnGestionEvenements.setPrefSize(300, 50);
        btnGestionEvenements.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        btnGestionEvenements.setOnAction(e -> ouvrirGestionEvenements(organisateur));

        Button btnNotifications = new Button("🔔 Notifications");
        btnNotifications.setPrefSize(300, 50);
        btnNotifications.setStyle(
                "-fx-background-color: #007bff; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        btnNotifications.setOnAction(e -> ouvrirNotifications(organisateur));

        Button btnStatistiques = new Button("📊 Statistiques");
        btnStatistiques.setPrefSize(300, 50);
        btnStatistiques.setStyle(
                "-fx-background-color: #fd7e14; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        btnStatistiques.setOnAction(e -> ouvrirStatistiques(organisateur));

        Button btnQuitter = new Button("❌ Quitter");
        btnQuitter.setPrefSize(300, 50);
        btnQuitter.setStyle(
                "-fx-background-color: #dc3545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        btnQuitter.setOnAction(e -> stagePrincipal.close());

        // Ajouter des effets de survol pour tous les boutons
        ajouterEffetsSurvol(btnGestionEvenements, "#218838", "#28a745");
        ajouterEffetsSurvol(btnNotifications, "#0056b3", "#007bff");
        ajouterEffetsSurvol(btnStatistiques, "#e55e0b", "#fd7e14");
        ajouterEffetsSurvol(btnQuitter, "#c82333", "#dc3545");

        boutonsNav.getChildren().addAll(
                btnGestionEvenements,
                btnNotifications,
                btnStatistiques,
                btnQuitter
        );

        // Section informative
        Label lblInfo = new Label("💡 Choisissez une option pour commencer la gestion de vos événements");
        lblInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lblInfo.setTextFill(Color.WHITE);
        lblInfo.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 5;");

        root.getChildren().addAll(titre, infoOrganisateur, boutonsNav, lblInfo);

        Scene scene = new Scene(root, 600, 700);
        stagePrincipal.setTitle("Gestion d'Événements - Accueil");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void ajouterEffetsSurvol(Button bouton, String couleurSurvol, String couleurNormale) {
        bouton.setOnMouseEntered(e -> {
            bouton.setStyle(bouton.getStyle().replace(couleurNormale, couleurSurvol) +
                    " -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });

        bouton.setOnMouseExited(e -> {
            bouton.setStyle(bouton.getStyle().replace(couleurSurvol, couleurNormale) +
                    " -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
        });
    }

    private void ouvrirGestionEvenements(Organisateur organisateur) {
        try {
            if (organisateur == null) {
                throw new IllegalArgumentException("L'organisateur ne peut pas être null");
            }
            if (stagePrincipal == null) {
                throw new IllegalStateException("La fenêtre principale (stagePrincipal) n'est pas initialisée");
            }
            if (interfaceOrganisateur == null) {
                interfaceOrganisateur = new InterfaceGestionOrganisateur(organisateur); // Passer l'organisateur
            }
            interfaceOrganisateur.afficherInterface(organisateur, stagePrincipal);
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ouverture de la gestion d'événements",
                    "Impossible d'ouvrir l'interface de gestion des événements.\n" +
                            "Détails: " + e.getMessage());
        }
    }

    private void ouvrirNotifications(Organisateur organisateur) {
        try {
            if (interfaceNotifications == null) {
                interfaceNotifications = new InterfaceNotifications();
            }
            interfaceNotifications.afficherInterface(organisateur, stagePrincipal);
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ouverture des notifications",
                    "Impossible d'ouvrir l'interface des notifications.\n" +
                            "Détails: " + e.getMessage());
        }
    }

    private void ouvrirStatistiques(Organisateur organisateur) {
        // Créer une fenêtre de statistiques simple
        Stage stageStats = new Stage();
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label titre = new Label("📊 Statistiques des Événements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titre.setTextFill(Color.DARKBLUE);

        // Informations statistiques de base
        VBox statsBox = new VBox(10);
        statsBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label lblNombreEvenements = new Label("🎪 Nombre d'événements: " +
                (organisateur.getEvenementsOrganises() != null ?
                        organisateur.getEvenementsOrganises().size() : 0));
        lblNombreEvenements.setFont(Font.font("Arial", 14));

        Label lblStatut = new Label("✅ Statut: Organisateur actif");
        lblStatut.setFont(Font.font("Arial", 14));
        lblStatut.setTextFill(Color.GREEN);

        Label lblDerniereMaj = new Label("🕒 Dernière mise à jour: " +
                java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblDerniereMaj.setFont(Font.font("Arial", 12));
        lblDerniereMaj.setTextFill(Color.GRAY);

        statsBox.getChildren().addAll(lblNombreEvenements, lblStatut, lblDerniereMaj);

        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20;");
        btnFermer.setOnAction(e -> stageStats.close());

        root.getChildren().addAll(titre, statsBox, btnFermer);

        Scene scene = new Scene(root, 400, 300);
        stageStats.setTitle("Statistiques");
        stageStats.setScene(scene);
        stageStats.show();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
