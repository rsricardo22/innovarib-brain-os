package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(name = "assignee_user_id")
    private UUID assigneeUserId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enums.TaskStatus status = Enums.TaskStatus.TODO;

}
