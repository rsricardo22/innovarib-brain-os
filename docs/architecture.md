# Arquitectura MVP - Innovarib Brain / Business Agent OS

## Objetivo

Innovarib Brain es un Business Agent OS multi-tenant para automatizar flujos empresariales con IA bajo un modelo **MVP nivel 2: la IA redacta y el humano aprueba**. La primera versión debe ser funcional, vendible y fácil de operar, evitando una arquitectura de microservicios prematura.

## Principios de diseño

- **Multi-tenant desde el inicio**: todo dato operativo pertenece a un tenant y se accede con contexto explícito.
- **Modular monolith primero**: un backend Spring Boot 3 organizado por módulos internos, listo para separar microservicios después.
- **Human-in-the-loop**: ningún resultado crítico generado por IA se ejecuta sin revisión/aprobación humana.
- **Auditable por defecto**: todas las acciones humanas, llamadas a herramientas y decisiones asistidas por IA generan trazabilidad.
- **Integraciones desacopladas**: eventos RabbitMQ para procesos asíncronos, Redis para caché/estado temporal y MinIO para archivos.
- **Seguridad por capas**: autenticación, autorización por roles/permisos, aislamiento de tenant y registro de auditoría.

## Vista lógica

```text
┌────────────────────────────┐
│ Frontend Angular 18/19     │
│ - Dashboard                │
│ - Workbench IA             │
│ - Bandeja de aprobaciones  │
│ - Admin tenant             │
└──────────────┬─────────────┘
               │ HTTPS / REST
┌──────────────▼─────────────┐
│ Backend Spring Boot 3      │
│ Modular Monolith           │
│                            │
│ Módulos MVP:               │
│ - identity & tenants       │
│ - users, roles, permissions│
│ - agent workspace          │
│ - approval workflow        │
│ - audit log                │
│ - integrations/events      │
└───┬──────────┬──────────┬───┘
    │          │          │
    │          │          │
┌───▼───┐  ┌───▼────┐  ┌──▼──────┐
│Postgres│ │ Redis  │  │RabbitMQ │
│OLTP    │ │ Cache  │  │Events   │
└───┬────┘ └────────┘  └─────────┘
    │
┌───▼────┐
│ MinIO  │
│ Files  │
└────────┘
```

## Componentes MVP

### Frontend Angular

Aplicación web para usuarios de negocio y administradores:

- Inicio de sesión.
- Selección o resolución automática del tenant.
- Panel de solicitudes IA.
- Editor de borradores generados por IA.
- Flujo de aprobación/rechazo.
- Historial auditable de acciones.

### Backend Spring Boot

Aplicación Java 17 con Spring Boot 3. La primera versión se implementa como monolito modular:

- `identity`: autenticación, usuarios y membresías por tenant.
- `tenancy`: resolución del tenant, validación de contexto y políticas de aislamiento.
- `workspace`: solicitudes de trabajo para agentes y resultados generados.
- `approvals`: estados de aprobación humana.
- `audit`: bitácora inmutable de eventos relevantes.
- `events`: publicación/consumo de eventos internos en RabbitMQ.
- `files`: almacenamiento de adjuntos en MinIO.

### PostgreSQL

Base principal transaccional para tenants, usuarios, solicitudes, aprobaciones, auditoría y metadatos de archivos.

### Redis

Uso inicial:

- Caché de sesión/token metadata si aplica.
- Rate limiting por tenant y usuario.
- Estado temporal de trabajos IA.

### RabbitMQ

Canal asíncrono para desacoplar tareas como generación de borradores, auditoría extendida, notificaciones y futuras integraciones.

### MinIO

Almacenamiento S3-compatible para documentos cargados por usuarios, artefactos generados y evidencia de aprobación.

## Flujo principal MVP

1. Un usuario autenticado crea una solicitud dentro de su tenant.
2. El backend valida permisos y registra auditoría.
3. Se crea un `agent_task` con estado `DRAFT_REQUESTED`.
4. El backend publica un evento para procesar el borrador.
5. El módulo IA genera una propuesta y la guarda como `agent_output`.
6. El usuario revisa el contenido en Angular.
7. El usuario aprueba, edita o rechaza.
8. El backend registra la decisión y emite eventos de seguimiento.

## Preparación para microservicios

Aunque el MVP es monolítico, los límites de módulo se diseñan para extracción futura:

- Identity/Tenancy como servicio de plataforma.
- Agent Workspace como servicio de orquestación IA.
- Approvals como workflow service.
- Audit como servicio append-only.
- Files como servicio documental.

Cada módulo debe exponer contratos internos claros y evitar acoplamiento directo a tablas de otros módulos salvo mediante servicios de aplicación.

## Decisiones iniciales

| Decisión | Elección MVP | Motivo |
| --- | --- | --- |
| Estilo backend | Monolito modular | Menor complejidad operativa inicial |
| API | REST JSON | Simple de vender, probar e integrar |
| Base de datos | PostgreSQL compartido con `tenant_id` | Multi-tenant rápido y controlable |
| Eventos | RabbitMQ | Desacoplamiento sin introducir Kafka |
| Archivos | MinIO | Compatible con S3 y local-friendly |
| IA | Router interno abstracto | Permite cambiar proveedor/modelo |

## No objetivos del MVP

- No implementar todos los microservicios independientes.
- No crear marketplace completo de agentes.
- No permitir acciones autónomas sin aprobación humana.
- No resolver analítica avanzada ni data warehouse en la primera versión.
