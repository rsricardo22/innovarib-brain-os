package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "emails")
public class Email extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(name = "campaign_id")
    private UUID campaignId;
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, columnDefinition = "text")
    private String body;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.EmailStatus status = Enums.EmailStatus.DRAFT;

}
