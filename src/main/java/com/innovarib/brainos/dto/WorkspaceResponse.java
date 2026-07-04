package com.innovarib.brainos.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkspaceResponse(UUID id, String tenantId, String name, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
