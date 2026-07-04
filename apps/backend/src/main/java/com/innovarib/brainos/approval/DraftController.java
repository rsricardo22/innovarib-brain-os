package com.innovarib.brainos.approval;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drafts")
public class DraftController {
    private final DraftService draftService;

    public DraftController(DraftService draftService) {
        this.draftService = draftService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Draft create(@RequestHeader("X-Tenant-Id") String tenantId, @Valid @RequestBody CreateDraftRequest request) {
        return draftService.create(tenantId, request);
    }

    @GetMapping
    List<Draft> list(@RequestHeader("X-Tenant-Id") String tenantId) {
        return draftService.list(tenantId);
    }

    @PostMapping("/{id}/approve")
    Draft approve(@RequestHeader("X-Tenant-Id") String tenantId, @PathVariable UUID id, @RequestBody DecisionRequest request) {
        return draftService.approve(tenantId, id, request);
    }

    @PostMapping("/{id}/reject")
    Draft reject(@RequestHeader("X-Tenant-Id") String tenantId, @PathVariable UUID id, @RequestBody DecisionRequest request) {
        return draftService.reject(tenantId, id, request);
    }
}
