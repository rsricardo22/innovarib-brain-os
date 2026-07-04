package com.innovarib.brainos.approval;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository extends JpaRepository<Draft, UUID> {
    List<Draft> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<Draft> findByIdAndTenantId(UUID id, String tenantId);
}
