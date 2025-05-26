package com.projetpoo.demotpfinale.modele;

public class Participant {
    private String id;
    private String nom;
    private String email;

    public Participant(String id, String nom, String email) {
        this.id=id;
        this.nom=nom;
        this.email=email;
    }
    //GETTERS
    public String getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }
    //SETTERS
    public void setId(String id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
