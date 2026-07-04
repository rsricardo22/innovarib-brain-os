package com.innovarib.brainos.agent;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    @GetMapping
    List<AgentSummary> list() {
        return List.of(new AgentSummary("sales-assistant", "Asistente Comercial", "Redacta respuestas comerciales para aprobación humana"));
    }

    record AgentSummary(String id, String name, String description) {
    }
}
