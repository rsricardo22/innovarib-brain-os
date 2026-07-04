package com.innovarib.brainos.controller;

import com.innovarib.brainos.dto.WorkspaceResponse;
import com.innovarib.brainos.service.WorkspaceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public List<WorkspaceResponse> list() {
        return workspaceService.listCurrentTenantWorkspaces();
    }
}
