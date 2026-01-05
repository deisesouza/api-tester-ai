package com.aitester.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final RestClient restClient;
    private final String openAiEndpoint;

    // injeta o Builder configurado
    public OpenAIService(RestClient.Builder builder) {
        this.restClient = builder.build();
        this.openAiEndpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
    }

    public String generateTestPayload(String endpointSchema) {
        // Prompt para a IA gerar um JSON de teste baseado no schema
        String prompt = "Gere um JSON de teste para o seguinte contrato: " + endpointSchema;

        return restClient.post()
                .uri(openAiEndpoint + "/openai/deployments/gpt-4/chat/completions?api-version=2023-05-15")
                .header("api-key", System.getenv("AZURE_OPENAI_KEY"))
                .body(Map.of("messages", List.of(Map.of("role", "user", "content", prompt))))
                .retrieve()
                .body(String.class);
    }

    public ResponseEntity<String> executeTargetTest(String path, String payload) {
        return restClient.post()
                .uri(System.getenv("TARGET_MICROSERVICE_URL") + path)
                .body(payload)
                .retrieve()
                .toEntity(String.class);
    }
}