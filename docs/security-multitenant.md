# Seguridad y multi-tenancy

## Modelo multi-tenant

El MVP usa un modelo de base de datos compartida con aislamiento lógico por `tenant_id`. Todas las tablas de negocio deben incluir `tenant_id`, excepto catálogos globales explícitos y tablas técnicas justificadas.

## Reglas obligatorias

- Toda petición autenticada debe resolver un `tenant_id` activo.
- El usuario solo puede operar tenants donde tenga membresía válida.
- Las consultas deben filtrar por `tenant_id` en la capa de repositorio o mediante filtros globales controlados.
- Los eventos deben incluir `tenant_id`, `actor_id`, `correlation_id` y `event_id`.
- Los archivos en MinIO deben guardarse con prefijo por tenant.
- Redis keys deben incluir prefijo de tenant cuando contengan datos de negocio.

## Identidad y autorización

Roles iniciales:

| Rol | Permisos esperados |
| --- | --- |
| OWNER | Control total del tenant, usuarios, facturación y configuración. |
| ADMIN | Gestión operativa, usuarios no owner y configuración funcional. |
| MEMBER | Uso de agentes, documentos, tareas y aprobaciones asignadas. |
| VIEWER | Lectura limitada sin acciones de aprobación o ejecución. |

La autorización debe combinar:

- Tenant activo.
- Rol del usuario.
- Permisos granulares.
- Ownership o asignación del recurso.
- Estado del recurso.
- Políticas de aprobación.

## Human-in-the-loop

Acciones sensibles requieren aprobación humana:

- Enviar emails a clientes.
- Crear o actualizar oportunidades comerciales críticas.
- Publicar contenido externo.
- Ejecutar webhooks o herramientas externas.
- Modificar configuraciones del tenant.

Estados recomendados:

- `DRAFT`.
- `PENDING_REVIEW`.
- `CHANGES_REQUESTED`.
- `APPROVED`.
- `REJECTED`.
- `EXECUTED`.
- `FAILED`.

## Auditoría

Deben auditarse:

- Login, logout y fallos de autenticación.
- Cambios de roles y permisos.
- Creación, edición y eliminación de datos críticos.
- Prompts enviados a modelos.
- Respuestas de modelos.
- Contexto documental usado por la IA.
- Aprobaciones, rechazos y ediciones humanas.
- Ejecuciones de herramientas externas.

Campos mínimos:

- `id`.
- `tenant_id`.
- `actor_type` (`USER`, `AGENT`, `SYSTEM`).
- `actor_id`.
- `action`.
- `resource_type`.
- `resource_id`.
- `metadata` JSONB.
- `ip_address`.
- `user_agent`.
- `correlation_id`.
- `created_at`.

## Protección de datos

- Cifrado en tránsito mediante TLS en ambientes reales.
- Cifrado en reposo gestionado por infraestructura o proveedor cloud.
- Secrets fuera del repositorio y cargados por variables de entorno.
- No registrar secretos, tokens ni documentos completos en logs.
- Enmascarar PII en trazas y errores.
- Definir retención por tipo de dato.

## MinIO

Estructura recomendada:

```text
s3://innovarib-brain/tenants/<tenant_id>/documents/<document_id>/<filename>
s3://innovarib-brain/tenants/<tenant_id>/exports/<export_id>.zip
```

## Redis

Convención de keys:

```text
tenant:<tenant_id>:session:<session_id>
tenant:<tenant_id>:rate-limit:<user_id>
tenant:<tenant_id>:agent-run:<run_id>
```

## RabbitMQ

- Exchanges por dominio.
- Routing keys versionadas.
- Dead-letter queues para procesos críticos.
- Payloads sin secretos.
- Idempotencia mediante `event_id`.
