package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.config.ApiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoadmapService {



    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> generateRoadmap(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            return Collections.singletonMap("error", "Topic is required");
        }

        String prompt = buildRoadmapPrompt(topic);
        HttpEntity<Map<String, Object>> entity = createRequestEntity(prompt);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiConfig.getUrl(),  // Using the injected URL
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            return processRoadmapResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            return Collections.singletonMap(
                    "error",
                    "HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()
            );
        } catch (Exception e) {
            return Collections.singletonMap("error", "Unexpected error: " + e.getMessage());
        }
    }

    private String buildRoadmapPrompt(String topic) {
        return String.format(
                "Generate a detailed roadmap for %s in a hierarchical JSON format. The structure should start with a root node having a \"title\" property, and each topic should be represented as an object with a \"title\" property and an optional \"children\" array containing subtopics. Ensure the output is strictly valid JSON without any additional explanations or text.\n\n" +
                        "The roadmap should include the following:\n" +
                        "1. **Major Categories**: Break down the topic into high-level categories (e.g., \"Basics\", \"Tools\", \"Frameworks\").\n" +
                        "2. **Subtopics**: Each category should have subtopics that provide more specific details.\n" +
                        "3. **Actionable Steps**: Include actionable steps or tasks under each subtopic.\n" +
                        "4. **Prerequisites**: Mention prerequisites for each category or subtopic.\n" +
                        "5. **Tools and Resources**: Suggest tools, libraries, frameworks, or resources.\n" +
                        "6. **Milestones**: Include milestones or checkpoints to track progress.\n\n" +
                        "Example format:\n" +
                        "{\n" +
                        "  \"title\": \"Sample Roadmap\",\n" +
                        "  \"children\": [\n" +
                        "    {\n" +
                        "      \"title\": \"1. Basics\",\n" +
                        "      \"children\": [\n" +
                        "        {\"title\": \"Learn Fundamentals\", \"tools\": [\"Tool1\", \"Tool2\"]}\n" +
                        "      ],\n" +
                        "      \"prerequisites\": [\"Basic knowledge\"]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}",
                topic
        );
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiConfig.getKey());  // Using the injected key
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "CareerPilot/1.0");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", apiConfig.getModel());
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("response_format", Map.of("type", "json_object"));

        return new HttpEntity<>(requestBody, headers);
    }

    private Map<String, Object> processRoadmapResponse(Map<String, Object> responseBody) {
        if (responseBody == null || !responseBody.containsKey("choices")) {
            return Collections.singletonMap("error", "Invalid API response");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices.isEmpty() || !choices.get(0).containsKey("message")) {
            return Collections.singletonMap("error", "No message in response");
        }

        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");

        try {
            // Clean the response (remove markdown code blocks if present)
            content = content.replaceAll("```json|```", "").trim();
            return (Map<String, Object>) new ObjectMapper().readValue(content, Map.class);
        } catch (Exception e) {
            return Collections.singletonMap("error", "Failed to parse roadmap: " + e.getMessage());
        }
    }
}
