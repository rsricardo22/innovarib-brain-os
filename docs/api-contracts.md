# Contratos API

## Cabeceras

- `Authorization: Bearer <jwt>` en producción.
- `X-Tenant-Id: <uuid-or-slug>` en modo desarrollo/MVP.
- `X-Correlation-Id` opcional; si no llega, el backend lo genera.

## Endpoints MVP

### Health

`GET /api/health`

Respuesta `200`:

```json
{ "status": "UP", "service": "innovarib-brain-os" }
```

### Listar agentes

`GET /api/agents`

### Crear borrador IA

`POST /api/drafts`

```json
{
  "agentId": "sales-assistant",
  "objective": "Responder a un lead interesado en automatización",
  "input": "Cliente pyme de servicios"
}
```

Respuesta `201`:

```json
{
  "id": "uuid",
  "status": "PENDING_APPROVAL",
  "content": "Borrador generado..."
}
```

### Aprobar/Rechazar

- `POST /api/drafts/{id}/approve`
- `POST /api/drafts/{id}/reject`

```json
{ "comment": "Aprobado por ventas" }
```
