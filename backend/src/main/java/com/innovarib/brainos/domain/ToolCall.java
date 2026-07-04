package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tool_calls")
public class ToolCall extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(name = "agent_run_id", nullable = false)
    private UUID agentRunId;
    @Column(name = "tool_name", nullable = false)
    private String toolName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.ToolCallStatus status = Enums.ToolCallStatus.QUEUED;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String request;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String response;

}
