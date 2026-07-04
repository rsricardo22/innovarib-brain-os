# Diseño de base de datos

## Convenciones

- Todas las tablas de negocio incluyen `tenant_id`.
- IDs UUID generados por aplicación.
- Campos de auditoría: `created_at`, `updated_at`, `created_by`, `updated_by`.
- Índices compuestos por `tenant_id` y campos de consulta frecuentes.

## Tablas MVP

| Tabla | Propósito |
| --- | --- |
| `tenants` | organizaciones cliente |
| `users` | usuarios humanos |
| `roles`, `permissions`, `role_permissions` | RBAC |
| `agents` | definición de agentes por tenant |
| `drafts` | contenido generado por IA pendiente de decisión |
| `approval_actions` | historial de aprobaciones/rechazos |
| `knowledge_sources` | documentos o sistemas fuente |
| `knowledge_chunks` | fragmentos indexables |
| `model_invocations` | trazabilidad de uso de modelos |
| `tool_invocations` | trazabilidad de herramientas |
| `audit_log` | eventos de seguridad y negocio |

## PostgreSQL y pgvector

El MVP puede iniciar sin pgvector para simplificar ejecución. La estrategia recomendada es agregar la extensión cuando se active RAG semántico real:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
ALTER TABLE knowledge_chunks ADD COLUMN embedding vector(1536);
```

## Aislamiento multi-tenant

La primera versión aplica filtros por `tenant_id` en repositorios y servicios. Para enterprise se recomienda agregar Row Level Security en PostgreSQL y políticas por tenant.
