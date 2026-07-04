# Eventos RabbitMQ

## Objetivo

RabbitMQ desacopla procesos que no deben bloquear la experiencia de usuario: generación de IA, parsing de documentos, indexación, notificaciones, auditoría, integraciones y ejecución de acciones aprobadas.

## Convenciones

- Eventos en JSON.
- Nombres versionados.
- Payloads pequeños, con referencias a recursos en PostgreSQL/MinIO.
- Todos los eventos incluyen `event_id`, `event_type`, `event_version`, `tenant_id`, `occurred_at` y `correlation_id`.
- Los consumidores son idempotentes.
- Los errores repetidos van a dead-letter queues.

## Envelope estándar

```json
{
  "event_id": "uuid",
  "event_type": "agent.run.requested",
  "event_version": 1,
  "tenant_id": "uuid",
  "actor_id": "uuid",
  "actor_type": "USER",
  "correlation_id": "uuid",
  "occurred_at": "2026-07-03T00:00:00Z",
  "payload": {}
}
```

## Exchanges iniciales

| Exchange | Tipo | Uso |
| --- | --- | --- |
| `brain.identity` | topic | Usuarios, membresías, permisos. |
| `brain.workspace` | topic | Cambios en CRM/tareas/notas. |
| `brain.agent` | topic | Ejecuciones de agentes y resultados. |
| `brain.approval` | topic | Solicitudes y decisiones de aprobación. |
| `brain.knowledge` | topic | Documentos, parsing e indexación. |
| `brain.integration` | topic | Herramientas externas y webhooks. |
| `brain.audit` | topic | Auditoría y eventos de seguridad. |
| `brain.notification` | topic | Notificaciones internas/email. |

## Eventos MVP

### Identidad

- `identity.user.invited.v1`.
- `identity.user.joined_tenant.v1`.
- `identity.role.changed.v1`.

### Workspace

- `workspace.account.created.v1`.
- `workspace.contact.created.v1`.
- `workspace.task.created.v1`.
- `workspace.note.created.v1`.

### Agentes

- `agent.run.requested.v1`.
- `agent.run.started.v1`.
- `agent.run.completed.v1`.
- `agent.run.failed.v1`.
- `agent.draft.created.v1`.

### Aprobaciones

- `approval.request.created.v1`.
- `approval.request.approved.v1`.
- `approval.request.rejected.v1`.
- `approval.request.changes_requested.v1`.
- `approval.action.execution_requested.v1`.
- `approval.action.executed.v1`.
- `approval.action.failed.v1`.

### Conocimiento

- `knowledge.document.uploaded.v1`.
- `knowledge.document.parse_requested.v1`.
- `knowledge.document.parsed.v1`.
- `knowledge.document.index_requested.v1`.
- `knowledge.document.indexed.v1`.
- `knowledge.document.failed.v1`.

### Integraciones

- `integration.tool.invocation_requested.v1`.
- `integration.tool.invocation_succeeded.v1`.
- `integration.tool.invocation_failed.v1`.
- `integration.webhook.received.v1`.

### Auditoría

- `audit.log.created.v1`.
- `audit.security_event.created.v1`.

## Colas iniciales

| Queue | Binding | Consumidor |
| --- | --- | --- |
| `agent-runs` | `brain.agent:agent.run.requested.*` | Worker de agentes. |
| `approval-actions` | `brain.approval:approval.action.execution_requested.*` | Executor controlado. |
| `document-parsing` | `brain.knowledge:knowledge.document.parse_requested.*` | Parser de documentos. |
| `document-indexing` | `brain.knowledge:knowledge.document.index_requested.*` | Indexador/RAG. |
| `notifications` | `brain.notification:#` | Servicio de notificaciones. |
| `audit-writer` | `brain.audit:#` | Persistencia de auditoría. |

## Dead-letter queues

Cada cola crítica debe tener DLQ:

- `agent-runs.dlq`.
- `approval-actions.dlq`.
- `document-parsing.dlq`.
- `document-indexing.dlq`.
- `notifications.dlq`.

## Reintentos

- Errores transitorios: reintento con backoff exponencial.
- Errores de validación o permisos: no reintentar; enviar a auditoría y marcar fallo.
- Errores de proveedor externo: reintentar con límite y abrir circuito si se repiten.

## Idempotencia

Cada consumidor debe guardar eventos procesados o usar una clave natural de idempotencia:

```text
consumer_name + event_id
```

Para acciones externas, usar también `approved_action_id` como clave de idempotencia.
