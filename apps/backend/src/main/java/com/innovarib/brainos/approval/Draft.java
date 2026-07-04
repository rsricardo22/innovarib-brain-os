package com.innovarib.brainos.approval;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Draft {
    @Id
    private UUID id;
    private String tenantId;
    private String agentId;
    private String objective;
    private String input;
    private String content;
    @Enumerated(EnumType.STRING)
    private DraftStatus status;
    private Instant createdAt;
    private Instant decidedAt;
    private String decisionComment;

    protected Draft() {
    }

    public Draft(String tenantId, String agentId, String objective, String input, String content) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.agentId = agentId;
        this.objective = objective;
        this.input = input;
        this.content = content;
        this.status = DraftStatus.PENDING_APPROVAL;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getAgentId() { return agentId; }
    public String getObjective() { return objective; }
    public String getInput() { return input; }
    public String getContent() { return content; }
    public DraftStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDecidedAt() { return decidedAt; }
    public String getDecisionComment() { return decisionComment; }

    public void approve(String comment) {
        this.status = DraftStatus.APPROVED;
        this.decisionComment = comment;
        this.decidedAt = Instant.now();
    }

    public void reject(String comment) {
        this.status = DraftStatus.REJECTED;
        this.decisionComment = comment;
        this.decidedAt = Instant.now();
    }
}
