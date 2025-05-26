package com.projetpoo.demotpfinale.modele;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@JsonTypeName("Conference")
public class ConferenceImpl extends Conference {
    public ConferenceImpl() {
        super("", "", null, "", 0, "", new ArrayList<>());
    }

    public ConferenceImpl(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                          String theme, List<String> intervenants) {
        super(id, nom, date, lieu, capaciteMax, theme, intervenants);
    }
}