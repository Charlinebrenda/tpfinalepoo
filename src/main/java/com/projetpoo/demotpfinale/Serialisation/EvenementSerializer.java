package com.projetpoo.demotpfinale.Serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projetpoo.demotpfinale.modele.Evenement;

import java.io.File;
import java.io.IOException;
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

    public void serialize(Map<String, Evenement> evenements) throws IOException {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            System.out.println("Serializing " + evenements.size() + " events to: " + file.getAbsolutePath());
            for (Map.Entry<String, Evenement> entry : evenements.entrySet()) {
                Evenement evt = entry.getValue();
                System.out.println("Event: " + entry.getKey() + ", Name: " + evt.getNom() +
                        ", Type: " + evt.getClass().getSimpleName() + ", Date: " + evt.getDate());
            }
            objectMapper.writeValue(file, evenements);
            System.out.println("Serialization completed successfully");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            System.out.println("Written JSON: " + content);
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}