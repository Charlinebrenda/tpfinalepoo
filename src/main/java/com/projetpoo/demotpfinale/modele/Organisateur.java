package com.projetpoo.demotpfinale.modele;

import java.util.List;

public class Organisateur extends Participant{
    private List<Evenement>evenementsOrganises;
    public Organisateur(String id, String nom, String email, List<Evenement> evenementsOrganises) {
        super(id,nom,email);
        this.evenementsOrganises = evenementsOrganises;
    }
    //GETTERS
    public List<Evenement> getEvenementsOrganises() {
        return evenementsOrganises;
    }

    //SETTERS
    public void setEvenementsOrganises(List<Evenement> evenementsOrganises) {
        this.evenementsOrganises = evenementsOrganises;
    }

}
