package com.projetpoo.demotpfinale.Serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.projetpoo.demotpfinale.modele.Evenement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EvenementDeserializer {
    private final ObjectMapper objectMapper;
    private final String filePath;

    public EvenementDeserializer(String filePath) {
        this.filePath = filePath.trim();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        System.out.println("Deserializer initialized with path: " + new File(filePath).getAbsolutePath());
    }

    public Map<String, Evenement> deserialize() throws IOException {
        File file = new File(filePath);
        System.out.println("Deserializing from: " + file.getAbsolutePath());

        if (!file.exists() || file.length() == 0) {
            System.out.println("File not found or empty, returning empty map: " + filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            return new HashMap<>();
        }

        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            System.out.println("File content: " + content);

            TypeReference<Map<String, Evenement>> typeRef = new TypeReference<Map<String, Evenement>>() {};
            Map<String, Evenement> events = objectMapper.readValue(file, typeRef);

            System.out.println("Deserialized " + events.size() + " events");
            for (Map.Entry<String, Evenement> entry : events.entrySet()) {
                Evenement evt = entry.getValue();
                System.out.println("Loaded event: " + entry.getKey() + " - " + evt.getNom() +
                        " (" + evt.getClass().getSimpleName() + ") on " + evt.getDate());
            }
            return events;

        } catch (Exception e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            // Pas de backup, renvoyer une map vide
            return new HashMap<>();
        }
    }
}