package com.example.kafka_service.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Convertir un objet en chaîne JSON si nécessaire
    public static String ensureString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj != null) {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException("Erreur de conversion en chaîne JSON", e);
            }
        }
        return null;
    }

    // Fusionner deux objets JSON
    public static String mergeJsonObjects(String json1, String json2) {
        if (json1 == null || json2 == null) {
            return json1 != null ? json1 : json2;
        }
        try {
            JsonNode node1 = objectMapper.readTree(json1);
            JsonNode node2 = objectMapper.readTree(json2);
            ((ObjectNode) node1).setAll((ObjectNode) node2);
            return objectMapper.writeValueAsString(node1);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de fusion JSON", e);
        }
    }

    // Extraire un champ Long depuis un JSON
    public static Long extractLongField(String json, String fieldName) {
        try {
            JsonNode node = objectMapper.readTree(json);
            return node.get(fieldName).asLong();
        } catch (Exception e) {
            throw new RuntimeException("Champ manquant ou invalide : " + fieldName, e);
        }
    }
}
