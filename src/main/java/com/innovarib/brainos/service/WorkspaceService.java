package com.innovarib.brainos.service;

import com.innovarib.brainos.config.TenantContext;
import com.innovarib.brainos.dto.WorkspaceResponse;
import com.innovarib.brainos.mapper.WorkspaceMapper;
import com.innovarib.brainos.repository.WorkspaceRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceMapper workspaceMapper) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMapper = workspaceMapper;
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> listCurrentTenantWorkspaces() {
        return workspaceRepository.findByTenantId(TenantContext.getTenantId()).stream()
                .map(workspaceMapper::toResponse)
                .toList();
    }
}
