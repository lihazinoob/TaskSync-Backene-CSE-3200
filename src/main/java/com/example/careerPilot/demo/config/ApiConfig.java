package com.example.careerPilot.demo.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "groq.api")
public class ApiConfig {
    private String url;
    private String key;
    private String model;

    // Getters and Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
}