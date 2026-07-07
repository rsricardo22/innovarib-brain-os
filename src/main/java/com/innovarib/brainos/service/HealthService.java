package com.innovarib.brainos.service;

import com.innovarib.brainos.config.TenantContext;
import com.innovarib.brainos.dto.HealthResponse;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthResponse health() {
        return new HealthResponse("UP", "brain-os-backend", TenantContext.getTenantId(), OffsetDateTime.now());
    }
}
