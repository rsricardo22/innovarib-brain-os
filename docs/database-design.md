# Diseño de base de datos

## Principios

- PostgreSQL es la fuente de verdad del MVP.
- Todas las tablas de negocio incluyen `tenant_id`.
- Usar UUID como identificador público.
- Usar `created_at`, `updated_at` y, cuando aplique, `deleted_at` para soft delete.
- Usar JSONB para metadatos flexibles, no para reemplazar relaciones centrales.
- Crear índices compuestos por `tenant_id` y campos de búsqueda frecuente.

## Esquemas sugeridos

Para el MVP se puede usar un esquema principal `public` con prefijos claros por módulo. Si el producto crece, se pueden separar esquemas por dominio.

## Tablas principales

### Tenancy e identidad

- `tenants`: empresas/clientes.
- `users`: usuarios globales.
- `tenant_memberships`: relación usuario-tenant.
- `roles`: roles disponibles.
- `permissions`: permisos granulares.
- `role_permissions`: permisos por rol.
- `invitations`: invitaciones a tenants.

### Workspace y CRM básico

- `workspaces`: espacios de trabajo del tenant.
- `accounts`: empresas o cuentas comerciales.
- `contacts`: contactos de negocio.
- `deals`: oportunidades comerciales.
- `tasks`: tareas operativas.
- `notes`: notas asociadas a entidades.

### Agentes

- `agents`: catálogo de agentes disponibles.
- `agent_configs`: configuración de agente por tenant.
- `prompt_templates`: plantillas versionadas.
- `agent_runs`: ejecuciones de agentes.
- `agent_run_messages`: mensajes, prompts y respuestas.
- `agent_context_items`: referencias de contexto usado.

### Aprobaciones

- `drafts`: salidas propuestas por IA.
- `approval_requests`: solicitudes de aprobación.
- `approval_decisions`: decisiones humanas.
- `approved_actions`: acciones aprobadas y ejecutables.

### Conocimiento y archivos

- `documents`: metadatos de documentos.
- `document_versions`: versiones de archivo.
- `document_chunks`: fragmentos textuales para búsqueda/RAG.
- `knowledge_collections`: colecciones de conocimiento por workspace.

### Integraciones

- `integration_connections`: conexiones configuradas por tenant.
- `integration_credentials`: referencias seguras a secretos externos.
- `webhook_endpoints`: endpoints configurados.
- `tool_invocations`: ejecuciones de herramientas.

### Auditoría y uso

- `audit_logs`: bitácora general.
- `security_events`: eventos de seguridad.
- `usage_records`: medición de uso por tenant.
- `outbox_events`: patrón outbox para publicar eventos confiables.

## Campos base por tabla de negocio

```sql
id uuid primary key,
tenant_id uuid not null,
created_at timestamptz not null,
updated_at timestamptz not null,
deleted_at timestamptz null
```

## Índices recomendados

- `(tenant_id, id)` en tablas de negocio.
- `(tenant_id, created_at)` para listados recientes.
- `(tenant_id, status)` para bandejas de trabajo.
- `(tenant_id, assigned_to_user_id, status)` en tareas y aprobaciones.
- Índices GIN para JSONB solo donde existan consultas reales.
- Índices full-text para `documents` y `document_chunks` antes de incorporar vector search externo.

## Patrón outbox

Para evitar pérdida de eventos:

1. El caso de uso escribe cambios de negocio y un registro en `outbox_events` dentro de la misma transacción.
2. Un worker publica el evento a RabbitMQ.
3. Al publicar correctamente, marca el evento como enviado.
4. Los consumidores deben ser idempotentes por `event_id`.

## Multi-tenancy en consultas

Toda consulta de negocio debe tener un predicado equivalente a:

```sql
where tenant_id = :currentTenantId
```

No se deben aceptar `tenant_id` arbitrarios desde el frontend sin validarlos contra la membresía del usuario autenticado.

## Migraciones

Usar Flyway o Liquibase desde el inicio. Convención recomendada con Flyway:

```text
V001__create_identity_tables.sql
V002__create_workspace_tables.sql
V003__create_agent_tables.sql
V004__create_approval_tables.sql
V005__create_knowledge_tables.sql
V006__create_audit_outbox_tables.sql
```
