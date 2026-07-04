package com.innovarib.brainos.repository;

import com.innovarib.brainos.entity.Workspace;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    List<Workspace> findByTenantId(String tenantId);
}
