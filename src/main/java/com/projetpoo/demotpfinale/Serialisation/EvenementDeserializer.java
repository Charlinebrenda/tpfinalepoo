package com.projetpoo.demotpfinale.Serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.projetpoo.demotpfinale.modele.Evenement;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class EvenementDeserializer {
    private final ObjectMapper objectMapper;
    private final String filePath;

    public EvenementDeserializer(String filePath) {
        this.filePath = filePath.trim();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        System.out.println("Deserializer initialized with path: " + new File(filePath).getAbsolutePath());
    }

    public Map<String, Evenement> deserialize() throws IOException {
        File file = new File(filePath);
        System.out.println("Deserializing from: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists() + ", Size: " + file.length() + " bytes");

        if (!file.exists() || file.length() == 0) {
            System.out.println("File not found or empty, returning empty map");
            return new HashMap<>();
        }

        if (!file.canRead()) {
            throw new IOException("File is not readable: " + file.getAbsolutePath());
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            System.out.println("File content: [" + content + "]");
            Map<String, Evenement> events = objectMapper.readValue(content, new TypeReference<Map<String, Evenement>>() {});
            System.out.println("Deserialized " + events.size() + " events");
            for (Map.Entry<String, Evenement> entry : events.entrySet()) {
                System.out.println("Event: ID=" + entry.getKey() + ", Name=" + entry.getValue().getNom() +
                        ", Type=" + entry.getValue().getClass().getSimpleName());
            }
            return events;
        } catch (Exception e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to deserialize events: " + e.getMessage(), e);
        }
    }
}