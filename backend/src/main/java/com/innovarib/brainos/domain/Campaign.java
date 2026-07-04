package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "campaigns")
public class Campaign extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.CampaignStatus status = Enums.CampaignStatus.DRAFT;

}
