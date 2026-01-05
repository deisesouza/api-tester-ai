package com.aitester.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

        private OpenAIService openAIService;

        @Mock
        private RestClient.Builder restClientBuilder;

        @Mock
        private RestClient restClient;

        // Mocks para a interface fluida do RestClient
        @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;
        @Mock private RestClient.RequestBodySpec requestBodySpec;
        @Mock private RestClient.ResponseSpec responseSpec;

        @BeforeEach
        void setUp() {
            // Configura o builder para retornar o mock do restClient
            when(restClientBuilder.build()).thenReturn(restClient);

            // Instancia a service (ela lerá as variáveis de ambiente)
            openAIService = new OpenAIService(restClientBuilder);
        }

        @Test
        void shouldGenerateTestPayloadSuccessfully() {
            // GIVEN
            String schema = "{ \"name\": \"string\" }";
            String mockResponse = "{\"choices\": [{\"message\": {\"content\": \"{\\\"name\\\": \\\"teste\\\"}\"}}]}";

            // Mocking da cadeia fluida do RestClient para o OpenAI
            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.header(any(), any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(Map.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(String.class)).thenReturn(mockResponse);

            // WHEN
            String result = openAIService.generateTestPayload(schema);

            // THEN
            assertNotNull(result);
            assertEquals(mockResponse, result);
            verify(restClient).post();
        }

        @Test
        void shouldExecuteTargetTestSuccessfully() {
            // GIVEN
            String path = "/api/v1/resource";
            String payload = "{\"name\": \"teste\"}";
            ResponseEntity<String> expectedResponse = ResponseEntity.ok("Success");

            // Mocking da cadeia fluida para o Target Microservice
            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(String.class)).thenReturn(expectedResponse);

            // WHEN
            ResponseEntity<String> response = openAIService.executeTargetTest(path, payload);

            // THEN
            assertEquals(200, response.getStatusCode().value());
            assertEquals("Success", response.getBody());
            verify(restClient).post();
        }
}
