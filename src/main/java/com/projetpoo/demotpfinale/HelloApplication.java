package com.projetpoo.demotpfinale;

import com.projetpoo.demotpfinale.modele.Organisateur;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class HelloApplication extends Application {
    private Stage stagePrincipal;

    @Override
    public void start(Stage stage) {
        this.stagePrincipal = stage;

        // Créer un organisateur de test
        Organisateur organisateur = new Organisateur(
                "ORG001",
                "Jean Dupont",
                "jean.dupont@organisation.fr",
                new ArrayList<>()
        );

        // Afficher l'interface de connexion
        creerInterfaceConnexion(organisateur);
    }

    private void creerInterfaceConnexion(Organisateur organisateur) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Titre de l'application
        Label titre = new Label("Système de Gestion d'Événements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titre.setTextFill(Color.WHITE);
        titre.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        // Informations de l'organisateur connecté
        VBox infoOrganisateur = new VBox(10);
        infoOrganisateur.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 20; -fx-background-radius: 10;");

        Label lblBienvenue = new Label("👋 Bienvenue, " + organisateur.getNom());
        lblBienvenue.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblBienvenue.setTextFill(Color.DARKBLUE);

        Label lblEmail = new Label(" " + organisateur.getEmail());
        lblEmail.setFont(Font.font("Arial", 14));
        lblEmail.setTextFill(Color.GRAY);

        Label lblId = new Label(" ID: " + organisateur.getId());
        lblId.setFont(Font.font("Arial", 12));
        lblId.setTextFill(Color.GRAY);

        infoOrganisateur.getChildren().addAll(lblBienvenue, lblEmail, lblId);

        // Bouton pour accéder au menu principal
        Button btnContinuer = new Button("Accéder au Menu");
        btnContinuer.setPrefSize(300, 50);
        btnContinuer.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        btnContinuer.setOnAction(e -> ouvrirMenuPrincipal(organisateur));

        // Ajouter un effet de survol
        btnContinuer.setOnMouseEntered(e -> btnContinuer.setStyle(
                btnContinuer.getStyle().replace("#28a745", "#218838") +
                        " -fx-scale-x: 1.05; -fx-scale-y: 1.05;"
        ));
        btnContinuer.setOnMouseExited(e -> btnContinuer.setStyle(
                btnContinuer.getStyle().replace("#218838", "#28a745") +
                        " -fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));

        // Section informative
        Label lblInfo = new Label(" Cliquez pour accéder aux fonctionnalités de gestion");
        lblInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lblInfo.setTextFill(Color.WHITE);
        lblInfo.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 5;");

        root.getChildren().addAll(titre, infoOrganisateur, btnContinuer, lblInfo);

        Scene scene = new Scene(root, 600, 700);
        stagePrincipal.setTitle("Gestion d'Événements - Connexion");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void ouvrirMenuPrincipal(Organisateur organisateur) {
        // Créer le contrôleur avec les paramètres corrects
        HelloController controller = new HelloController(organisateur, stagePrincipal);

        // Créer la scène avec le contrôleur
        Scene scene = new Scene(controller, 600, 400);
        stagePrincipal.setTitle("Menu Principal - Gestion d'Événements");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    public static void main(String[] args) {
        launch();
    }
}