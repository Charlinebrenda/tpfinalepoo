package com.projetpoo.demotpfinale.modele;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.time.LocalDateTime;
@JsonTypeName("Concert")
public class ConcertImpl extends Concert {
    public ConcertImpl() {
        super("", "", null, "", 0, "", "");
    }

    public ConcertImpl(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                       String artiste, String genreMusical) {
        super(id, nom, date, lieu, capaciteMax, artiste, genreMusical);
    }
}