package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "leads")
public class Lead extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(nullable = false)
    private String name;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.LeadStatus status = Enums.LeadStatus.NEW;

}
