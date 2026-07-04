# Contratos API MVP

## Convenciones generales

- Protocolo: REST sobre HTTPS.
- Formato: JSON UTF-8.
- Autenticación: `Authorization: Bearer <token>`.
- Tenant: `X-Tenant-Id` o resolución por subdominio/slug en una etapa posterior.
- Correlación: aceptar y devolver `X-Correlation-Id`.
- Versionado: prefijo `/api/v1`.
- Errores: formato uniforme `ApiError`.

## Modelo de error

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "details": [
    {
      "field": "title",
      "message": "must not be blank"
    }
  ],
  "correlationId": "9ac9b7de-65c6-41bb-88a7-0bc8f70b6d8f"
}
```

Códigos HTTP esperados:

- `200 OK`: lectura o acción completada.
- `201 Created`: recurso creado.
- `202 Accepted`: proceso asíncrono iniciado.
- `400 Bad Request`: entrada inválida.
- `401 Unauthorized`: falta autenticación.
- `403 Forbidden`: sin permiso o tenant inválido.
- `404 Not Found`: recurso inexistente dentro del tenant.
- `409 Conflict`: transición de estado inválida.
- `429 Too Many Requests`: límite por tenant/usuario.
- `500 Internal Server Error`: error inesperado.

## Autenticación

### POST /api/v1/auth/login

Solicitud:

```json
{
  "email": "admin@acme.com",
  "password": "secret"
}
```

Respuesta `200`:

```json
{
  "accessToken": "jwt",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "uuid",
    "email": "admin@acme.com",
    "fullName": "Admin Acme"
  },
  "tenants": [
    {
      "id": "uuid",
      "slug": "acme",
      "name": "Acme Inc",
      "role": "TENANT_ADMIN"
    }
  ]
}
```

### GET /api/v1/me

Devuelve el usuario actual y sus membresías.

## Tenants

### GET /api/v1/tenants/current

Headers:

```text
Authorization: Bearer <token>
X-Tenant-Id: <tenant-id>
```

Respuesta:

```json
{
  "id": "uuid",
  "slug": "acme",
  "name": "Acme Inc",
  "status": "ACTIVE"
}
```

## Agent Tasks

### POST /api/v1/agent-tasks

Crea una solicitud para que la IA redacte un borrador.

Permiso requerido: `agent_task:create`.

Solicitud:

```json
{
  "title": "Responder solicitud de propuesta",
  "taskType": "PROPOSAL_DRAFT",
  "input": {
    "customerName": "Globex",
    "goal": "Preparar respuesta comercial inicial",
    "context": "Cliente interesado en automatización de soporte"
  }
}
```

Respuesta `202`:

```json
{
  "id": "uuid",
  "title": "Responder solicitud de propuesta",
  "taskType": "PROPOSAL_DRAFT",
  "status": "DRAFT_REQUESTED",
  "correlationId": "uuid",
  "createdAt": "2026-07-03T00:00:00Z"
}
```

### GET /api/v1/agent-tasks

Lista paginada filtrada por tenant.

Query params:

- `status` opcional.
- `taskType` opcional.
- `page` por defecto `0`.
- `size` por defecto `20`.

Respuesta:

```json
{
  "items": [
    {
      "id": "uuid",
      "title": "Responder solicitud de propuesta",
      "taskType": "PROPOSAL_DRAFT",
      "status": "PENDING_HUMAN_APPROVAL",
      "createdAt": "2026-07-03T00:00:00Z",
      "updatedAt": "2026-07-03T00:01:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total": 1
}
```

### GET /api/v1/agent-tasks/{id}

Devuelve detalle de tarea, último output y aprobación asociada.

Respuesta:

```json
{
  "id": "uuid",
  "title": "Responder solicitud de propuesta",
  "taskType": "PROPOSAL_DRAFT",
  "status": "PENDING_HUMAN_APPROVAL",
  "input": {},
  "latestOutput": {
    "id": "uuid",
    "contentType": "text/markdown",
    "output": {
      "draft": "Texto propuesto por IA"
    },
    "riskLevel": "LOW",
    "modelProvider": "openai",
    "modelName": "configured-model",
    "createdAt": "2026-07-03T00:01:00Z"
  },
  "approval": {
    "id": "uuid",
    "status": "PENDING"
  }
}
```

## Aprobaciones

### GET /api/v1/approvals

Lista aprobaciones pendientes o históricas por tenant.

Query params:

- `status`, por defecto `PENDING`.
- `page`.
- `size`.

### POST /api/v1/approvals/{id}/approve

Aprueba un borrador generado por IA.

Permiso requerido: `approval:decide`.

Solicitud:

```json
{
  "comment": "Aprobado para enviar al cliente"
}
```

Respuesta:

```json
{
  "id": "uuid",
  "status": "APPROVED",
  "decidedAt": "2026-07-03T00:05:00Z"
}
```

### POST /api/v1/approvals/{id}/reject

Rechaza un borrador.

Solicitud:

```json
{
  "comment": "Necesita tono más técnico"
}
```

Respuesta:

```json
{
  "id": "uuid",
  "status": "REJECTED",
  "decidedAt": "2026-07-03T00:05:00Z"
}
```

## Archivos

### POST /api/v1/files/presigned-upload

Genera una URL prefirmada para subir un archivo a MinIO.

Solicitud:

```json
{
  "filename": "brief.pdf",
  "contentType": "application/pdf",
  "sizeBytes": 204800
}
```

Respuesta:

```json
{
  "fileId": "uuid",
  "uploadUrl": "http://minio:9000/bucket/key?signature=...",
  "objectKey": "tenants/acme/uploads/uuid/brief.pdf",
  "expiresIn": 900
}
```

## Auditoría

### GET /api/v1/audit-events

Permiso requerido: `audit:read`.

Query params:

- `entityType` opcional.
- `entityId` opcional.
- `action` opcional.
- `from` y `to` opcionales.

Respuesta:

```json
{
  "items": [
    {
      "id": "uuid",
      "action": "AGENT_TASK_CREATED",
      "entityType": "agent_task",
      "entityId": "uuid",
      "actorUserId": "uuid",
      "correlationId": "uuid",
      "createdAt": "2026-07-03T00:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total": 1
}
```

## Reglas de transición MVP

| Recurso | Desde | Hacia | Actor |
| --- | --- | --- | --- |
| agent_task | `DRAFT_REQUESTED` | `DRAFT_GENERATING` | sistema |
| agent_task | `DRAFT_GENERATING` | `PENDING_HUMAN_APPROVAL` | sistema |
| approval | `PENDING` | `APPROVED` | humano autorizado |
| approval | `PENDING` | `REJECTED` | humano autorizado |
| agent_task | `PENDING_HUMAN_APPROVAL` | `APPROVED` | backend tras aprobación |
| agent_task | `PENDING_HUMAN_APPROVAL` | `REJECTED` | backend tras rechazo |

## Requisitos de seguridad por endpoint

- Todos los endpoints salvo `/auth/login` requieren token válido.
- Todos los endpoints de negocio requieren tenant resuelto.
- El backend debe verificar membresía activa del usuario en el tenant.
- Un usuario no puede consultar IDs de otro tenant, incluso si conoce el UUID.
- Las respuestas de error no deben revelar existencia de recursos entre tenants.
