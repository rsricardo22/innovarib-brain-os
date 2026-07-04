package com.innovarib.brainos.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk extends BaseEntity {
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    @Column(name = "document_id", nullable = false)
    private UUID documentId;
    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "embedding", columnDefinition = "jsonb")
    private String embedding;

}
