package com.innovarib.brainos.agent;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    Map<String, String> health() {
        return Map.of("status", "UP", "service", "innovarib-brain-os");
    }
}
