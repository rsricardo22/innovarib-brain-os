package com.innovarib.brainos.approval;

import com.innovarib.brainos.config.RabbitConfig;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DraftService {
    private final DraftRepository drafts;
    private final RabbitTemplate rabbitTemplate;

    public DraftService(DraftRepository drafts, RabbitTemplate rabbitTemplate) {
        this.drafts = drafts;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Draft create(String tenantId, CreateDraftRequest request) {
        String content = "Propuesta generada para: " + request.objective()
                + "\n\nContexto: " + request.input()
                + "\n\nSiguiente paso sugerido: enviar este borrador tras aprobación humana.";
        Draft draft = drafts.save(new Draft(tenantId, request.agentId(), request.objective(), request.input(), content));
        publish("draft.created", tenantId, draft.getId());
        return draft;
    }

    @Transactional(readOnly = true)
    public List<Draft> list(String tenantId) {
        return drafts.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Transactional
    public Draft approve(String tenantId, UUID id, DecisionRequest request) {
        Draft draft = findDraft(tenantId, id);
        draft.approve(request.comment());
        publish("draft.approved", tenantId, draft.getId());
        return draft;
    }

    @Transactional
    public Draft reject(String tenantId, UUID id, DecisionRequest request) {
        Draft draft = findDraft(tenantId, id);
        draft.reject(request.comment());
        publish("draft.rejected", tenantId, draft.getId());
        return draft;
    }

    private Draft findDraft(String tenantId, UUID id) {
        return drafts.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new IllegalArgumentException("Draft not found"));
    }

    private void publish(String routingKey, String tenantId, UUID draftId) {
        rabbitTemplate.convertAndSend(RabbitConfig.EVENTS_EXCHANGE, routingKey, Map.of("tenantId", tenantId, "draftId", draftId.toString()));
    }
}
