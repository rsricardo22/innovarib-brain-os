# Seguridad y multi-tenant

## Autenticación

El MVP permite modo desarrollo con `X-Tenant-Id`. Producción debe usar OIDC/JWT con claims: `sub`, `tenant_id`, `roles`, `permissions`.

## Autorización

- RBAC por tenant.
- Permisos granulares: `draft:create`, `draft:approve`, `agent:manage`, `memory:manage`.
- Separar creador de aprobador para flujos críticos.

## Aislamiento de datos

- Validar tenant en cada request.
- No aceptar `tenant_id` en payload cuando pueda derivarse del token.
- Índices por tenant.
- Considerar PostgreSQL RLS para planes enterprise.

## Auditoría

Registrar login, creación de borradores, invocaciones de modelo, aprobaciones, rechazos, ejecución de herramientas y cambios de permisos.
