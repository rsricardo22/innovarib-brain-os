# Eventos RabbitMQ

## Exchange

`brainos.events` tipo topic.

## Routing keys

| Evento | Routing key | Productor | Consumidor futuro |
| --- | --- | --- | --- |
| Borrador creado | `draft.created` | Orchestrator | Notifications, Audit |
| Borrador aprobado | `draft.approved` | Approval | Tool Router |
| Borrador rechazado | `draft.rejected` | Approval | Analytics |
| Modelo invocado | `model.invoked` | Model Router | Billing |
| Herramienta ejecutada | `tool.executed` | Tool Router | Audit |

## Envelope

```json
{
  "eventId": "uuid",
  "tenantId": "tenant-demo",
  "type": "draft.created",
  "occurredAt": "2026-07-03T12:00:00Z",
  "correlationId": "uuid",
  "payload": {}
}
```
