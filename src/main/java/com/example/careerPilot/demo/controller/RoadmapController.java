package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;




    @PostMapping("/generate")
    public ResponseEntity<?> generateRoadmap(@RequestBody Map<String, String> request) {
        String topic = request.get("topic");
        Map<String, Object> response = roadmapService.generateRoadmap(topic);

        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}