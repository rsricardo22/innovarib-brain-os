# Diseño de Base de Datos MVP

## Estrategia multi-tenant

El MVP usa una base PostgreSQL compartida con aislamiento lógico por columna `tenant_id` en todas las tablas de negocio. Esta decisión reduce la complejidad operativa y permite migrar después a esquemas o bases separadas para tenants enterprise.

Reglas obligatorias:

- Toda consulta de negocio debe filtrar por `tenant_id`.
- Las claves únicas relevantes deben incluir `tenant_id`.
- El backend debe resolver el tenant antes de ejecutar casos de uso.
- Las tablas de auditoría deben registrar `tenant_id`, `actor_user_id`, `correlation_id` y origen.
- Se recomienda habilitar Row Level Security en una etapa posterior, una vez estabilizado el modelo.

## Entidades principales

### tenants

Representa una organización cliente.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| slug | varchar(80) | único, usado para resolución |
| name | varchar(160) | nombre comercial |
| status | varchar(30) | `ACTIVE`, `SUSPENDED`, `DELETED` |
| created_at | timestamptz | auditoría |
| updated_at | timestamptz | auditoría |

### users

Identidad global del usuario.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| email | varchar(255) | único global inicialmente |
| full_name | varchar(180) | nombre visible |
| password_hash | text | si se usa login local |
| status | varchar(30) | `ACTIVE`, `INVITED`, `DISABLED` |
| created_at | timestamptz | auditoría |
| updated_at | timestamptz | auditoría |

### tenant_memberships

Relación usuario-tenant.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| user_id | uuid | FK users |
| role | varchar(60) | rol base MVP |
| status | varchar(30) | `ACTIVE`, `DISABLED` |
| created_at | timestamptz | auditoría |

Índices:

- `unique (tenant_id, user_id)`
- `index (user_id)`

### roles

Roles configurables por tenant para preparar permisos finos.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| code | varchar(80) | único por tenant |
| name | varchar(120) | nombre visible |
| created_at | timestamptz | auditoría |

### permissions

Catálogo de permisos del sistema.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| code | varchar(120) | único |
| description | text | descripción |

### role_permissions

Asignación de permisos a roles.

| Campo | Tipo | Notas |
| --- | --- | --- |
| role_id | uuid | FK roles |
| permission_id | uuid | FK permissions |

PK compuesta: `(role_id, permission_id)`.

## Workspace IA

### agent_tasks

Solicitud de trabajo para IA.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| created_by_user_id | uuid | FK users |
| title | varchar(180) | resumen |
| task_type | varchar(80) | ejemplo: `EMAIL_DRAFT`, `PROPOSAL_DRAFT` |
| input_payload | jsonb | datos de entrada |
| status | varchar(40) | estado del flujo |
| correlation_id | uuid | trazabilidad |
| created_at | timestamptz | auditoría |
| updated_at | timestamptz | auditoría |

Estados iniciales:

- `DRAFT_REQUESTED`
- `DRAFT_GENERATING`
- `PENDING_HUMAN_APPROVAL`
- `APPROVED`
- `REJECTED`
- `FAILED`

### agent_outputs

Resultado redactado por IA.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| agent_task_id | uuid | FK agent_tasks |
| model_provider | varchar(80) | proveedor utilizado |
| model_name | varchar(120) | modelo utilizado |
| content_type | varchar(80) | `text/markdown`, `application/json` |
| output_payload | jsonb | respuesta estructurada |
| risk_level | varchar(30) | `LOW`, `MEDIUM`, `HIGH` |
| created_at | timestamptz | auditoría |

Índices:

- `index (tenant_id, agent_task_id)`

## Aprobaciones

### approvals

Decisión humana sobre un resultado IA.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| agent_task_id | uuid | FK agent_tasks |
| agent_output_id | uuid | FK agent_outputs |
| requested_by_user_id | uuid | FK users |
| decided_by_user_id | uuid | FK users nullable |
| status | varchar(40) | `PENDING`, `APPROVED`, `REJECTED`, `CHANGES_REQUESTED` |
| decision_comment | text | motivo o notas |
| decided_at | timestamptz | nullable |
| created_at | timestamptz | auditoría |

## Archivos

### files

Metadatos de objetos guardados en MinIO.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | FK tenants |
| uploaded_by_user_id | uuid | FK users |
| bucket | varchar(120) | bucket MinIO |
| object_key | text | ruta interna |
| original_filename | varchar(255) | nombre original |
| content_type | varchar(120) | MIME |
| size_bytes | bigint | tamaño |
| sha256 | varchar(64) | integridad |
| created_at | timestamptz | auditoría |

## Auditoría

### audit_events

Registro append-only de acciones importantes.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | uuid | PK |
| tenant_id | uuid | nullable para eventos globales |
| actor_user_id | uuid | nullable para eventos del sistema |
| action | varchar(120) | ejemplo: `AGENT_TASK_CREATED` |
| entity_type | varchar(80) | tipo de entidad |
| entity_id | uuid | id entidad |
| metadata | jsonb | datos no sensibles |
| ip_address | inet | origen |
| user_agent | text | origen |
| correlation_id | uuid | trazabilidad |
| created_at | timestamptz | timestamp inmutable |

## Convenciones técnicas

- IDs UUID generados por backend o PostgreSQL.
- Fechas en UTC con `timestamptz`.
- JSONB para payloads flexibles, pero no para datos que requieran filtros frecuentes.
- Índices mínimos por `tenant_id`, FK y estados de workflow.
- Soft delete solo donde aporte valor; para auditoría se prefiere append-only.

## Migraciones iniciales

Cuando se cree el backend, usar Flyway o Liquibase con migraciones versionadas:

```text
V1__create_tenants_and_identity.sql
V2__create_agent_workspace.sql
V3__create_approvals.sql
V4__create_files.sql
V5__create_audit_events.sql
```
