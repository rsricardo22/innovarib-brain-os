package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent_runs")
public class AgentRun extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(name = "agent_name", nullable = false)
    private String agentName;
    @Column(name = "started_by_user_id")
    private UUID startedByUserId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.AgentRunStatus status = Enums.AgentRunStatus.QUEUED;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String input;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String output;

}
