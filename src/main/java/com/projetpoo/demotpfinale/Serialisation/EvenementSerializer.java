package com.projetpoo.demotpfinale.Serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.projetpoo.demotpfinale.modele.Evenement;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class EvenementSerializer {
    private final ObjectMapper objectMapper;
    private final String filePath;

    public EvenementSerializer(String filePath) {
        this.filePath = filePath.trim();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        System.out.println("Serializer initialized with path: " + new File(filePath).getAbsolutePath());
    }

    public void serialize(Map<String, Evenement> nouveauxEvenements) throws IOException {
        File file = new File(filePath);
        System.out.println("Serializing to: " + file.getAbsolutePath());

        // Créer le répertoire parent si nécessaire
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Vérifier les permissions
        if (file.exists() && !file.canWrite()) {
            throw new IOException("File is not writable: " + file.getAbsolutePath());
        }

        // Étape 1 : Lire les événements existants
        Map<String, Evenement> evenementsExistants = new HashMap<>();
        if (file.exists() && file.length() > 0) {
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                System.out.println("Reading existing events: [" + content + "]");
                evenementsExistants = objectMapper.readValue(content, new TypeReference<Map<String, Evenement>>() {});
                System.out.println("Loaded " + evenementsExistants.size() + " existing events");
            } catch (Exception e) {
                System.err.println("Failed to read existing events: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No existing file or empty, starting with empty map");
        }

        // Étape 2 : Fusionner les nouveaux événements
        evenementsExistants.putAll(nouveauxEvenements);
        System.out.println("Merged " + nouveauxEvenements.size() + " new events, total: " + evenementsExistants.size());

        // Étape 3 : Écrire la map fusionnée
        try {
            objectMapper.writeValue(file, evenementsExistants);
            String writtenContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            System.out.println("Serialization successful, written JSON: [" + writtenContent + "]");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}