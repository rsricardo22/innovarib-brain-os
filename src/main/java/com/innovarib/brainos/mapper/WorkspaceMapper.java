package com.innovarib.brainos.mapper;

import com.innovarib.brainos.dto.WorkspaceResponse;
import com.innovarib.brainos.entity.Workspace;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceMapper {

    public WorkspaceResponse toResponse(Workspace workspace) {
        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getTenantId(),
                workspace.getName(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt());
    }
}
