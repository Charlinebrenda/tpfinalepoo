package com.projetpoo.demotpfinale.modele;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                      String theme, List<String> intervenants) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = new ArrayList<>(intervenants);
    }
    @JsonProperty("Theme")
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    @JsonProperty("Intervenants")
    public List<String> getIntervenants() { return intervenants; }
    public void setIntervenants(List<String> intervenants) { this.intervenants = new ArrayList<>(intervenants); }
}