package com.innovarib.brainos.dto;

import java.time.OffsetDateTime;

public record HealthResponse(String status, String service, String tenantId, OffsetDateTime timestamp) {
}
