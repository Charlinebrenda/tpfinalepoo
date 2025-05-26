package com.projetpoo.demotpfinale;

import com.projetpoo.demotpfinale.Interface.InterfaceGestionOrganisateur;
import com.projetpoo.demotpfinale.Interface.InterfaceNotifications;
import com.projetpoo.demotpfinale.functions.InterfaceInscription;
import com.projetpoo.demotpfinale.modele.Organisateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloController extends VBox {
    private Organisateur organisateur;
    private Stage stagePrincipal;
    private InterfaceGestionOrganisateur interfaceGestionOrganisateur;
    private InterfaceNotifications interfaceNotifications;

    public HelloController(Organisateur organisateur, Stage stagePrincipal) {
        this.organisateur = organisateur;
        this.stagePrincipal = stagePrincipal;

        // Configuration du layout principal
        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        // Construction de l'interface
        construireInterface();
    }

    private void construireInterface() {
        // Titre du menu
        Label titre = new Label(" Menu Principal");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);
        titre.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        // Informations de l'organisateur
        Label lblOrganisateur = new Label("Organisateur: " + organisateur.getNom());
        lblOrganisateur.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lblOrganisateur.setTextFill(Color.GRAY);

        // Création des boutons du menu
        Button btnInscription = creerBouton(" Inscription des participants", "#28a745");
        Button btnGestionOrganisateur = creerBouton(" Gestion des organisateurs", "#007bff");
        Button btnNotifications = creerBouton("Notifications en temps réel", "#ffc107");
        Button btnPersistance = creerBouton("Persistance des données", "#6f42c1");
        Button btnRetour = creerBouton(" Retour à l'accueil", "#6c757d");

        // Configuration des actions des boutons
        btnInscription.setOnAction(e -> ouvrirInscription());
        btnGestionOrganisateur.setOnAction(e -> ouvrirGestionOrganisateur());
        btnNotifications.setOnAction(e -> ouvrirNotifications());
        btnPersistance.setOnAction(e -> ouvrirPersistance());
        btnRetour.setOnAction(e -> retourAccueil());

        // Ajout des éléments à l'interface
        this.getChildren().addAll(
                titre,
                lblOrganisateur,
                btnInscription,
                btnGestionOrganisateur,
                btnNotifications,
                btnPersistance,
                btnRetour
        );
    }

    private Button creerBouton(String texte, String couleur) {
        Button bouton = new Button(texte);
        bouton.setPrefSize(350, 50);
        bouton.setStyle(
                "-fx-background-color: " + couleur + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        // Effets de survol
        String couleurSurvol = obtenirCouleurSurvol(couleur);
        bouton.setOnMouseEntered(e -> bouton.setStyle(
                bouton.getStyle().replace(couleur, couleurSurvol) +
                        " -fx-scale-x: 1.03; -fx-scale-y: 1.03;"
        ));
        bouton.setOnMouseExited(e -> bouton.setStyle(
                bouton.getStyle().replace(couleurSurvol, couleur) +
                        " -fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));

        return bouton;
    }

    private String obtenirCouleurSurvol(String couleurOriginale) {
        switch (couleurOriginale) {
            case "#28a745": return "#218838";
            case "#007bff": return "#0056b3";
            case "#ffc107": return "#e0a800";
            case "#6f42c1": return "#5a32a3";
            case "#6c757d": return "#545b62";
            default: return couleurOriginale;
        }
    }

    private void ouvrirInscription() {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(new InterfaceInscription(), 600, 500);
            stage.setTitle("Inscription des Participants");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible d'ouvrir l'interface d'inscription: " + e.getMessage());
        }
    }

    private void ouvrirGestionOrganisateur() {
        try {
            if (interfaceGestionOrganisateur == null) {
                interfaceGestionOrganisateur = new InterfaceGestionOrganisateur(organisateur);
            }

            // Utiliser la méthode afficherInterface de InterfaceGestionOrganisateur
            interfaceGestionOrganisateur.afficherInterface(organisateur, stagePrincipal);

        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la gestion des organisateurs: " + e.getMessage());
        }
    }

    private void ouvrirNotifications() {
        try {
            if (interfaceNotifications == null) {
                interfaceNotifications = new InterfaceNotifications();
            }

            // Créer une nouvelle fenêtre pour les notifications
            Stage stage = new Stage();
            interfaceNotifications.afficherInterface(organisateur, stage);

        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible d'ouvrir les notifications: " + e.getMessage());
        }
    }

    private void ouvrirPersistance() {
        // Interface simple pour la persistance des données
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Persistance des données");
        alert.setHeaderText("Gestion des données");
        alert.setContentText("Fonctionnalité de persistance des données.\n" +
                "Les données sont actuellement stockées en mémoire.\n" +
                "Implémentation de la sauvegarde/chargement à venir.");
        alert.showAndWait();
    }

    private void retourAccueil() {
        try {
            // Retourner à l'écran de connexion principal
            HelloApplication app = new HelloApplication();
            app.start(stagePrincipal);
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de retourner à l'accueil: " + e.getMessage());
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}