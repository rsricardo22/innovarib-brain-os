package com.innovarib.brainos.approval;

import jakarta.validation.constraints.NotBlank;

public record CreateDraftRequest(@NotBlank String agentId, @NotBlank String objective, @NotBlank String input) {
}
