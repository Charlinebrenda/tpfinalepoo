package com.projetpoo.demotpfinale.modele;


import java.util.Map;

public class GestionEvenement {
    private static  GestionEvenement instance;
    private Map<String, Evenement> evenements;

    // Constructeur privé qui accepte une Map d'événements
    private GestionEvenement(Map<String, Evenement> evenements) {
        this.evenements = evenements;
    }
    // Méthode pour obtenir l'instance unique avec une Map initiale
    public static GestionEvenement getInstance(Map<String, Evenement> evenements) {
        if (instance == null) {
            instance = new GestionEvenement(evenements);
        }
        return instance;
    }
    // Méthode pour ajouter un événement
    public void ajouterEvenement(Evenement evenement) {
        evenements.put(evenement.getId(), evenement);

    }
    // Méthode pour supprimer un événement
    public void supprimerEvenement(String id) {
        evenements.remove(id);
    }
    // Méthode pour rechercher un événement
    public Evenement rechercherEvenement(String id) {
        return evenements.get(id);
    }
}

