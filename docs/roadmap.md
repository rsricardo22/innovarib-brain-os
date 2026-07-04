# Roadmap — Innovarib Brain / Business Agent OS

## Fase 0 — Base del proyecto

- Documentación inicial de arquitectura.
- Estructura de monorepo.
- Docker Compose con PostgreSQL, Redis, RabbitMQ y MinIO.
- Convenciones de módulos, eventos y seguridad.

## Fase 1 — MVP vendible

Objetivo: demostrar valor de IA asistida con revisión humana.

Entregables:

- Login y gestión básica de tenant.
- Roles `OWNER`, `ADMIN`, `MEMBER`, `VIEWER`.
- Workspace con contactos, cuentas, tareas y notas.
- Carga de documentos a MinIO.
- Generación de borradores por agente.
- Bandeja de revisión/aprobación.
- Auditoría de acciones principales.
- Eventos RabbitMQ mínimos.

Criterio de salida:

- Un cliente puede usar el sistema para generar y aprobar contenido empresarial real.

## Fase 2 — RAG y memoria operativa

Objetivo: mejorar la calidad de respuestas usando conocimiento del tenant.

Entregables:

- Extracción de texto de documentos.
- Indexación asíncrona.
- Estrategia de embeddings por tenant.
- Recuperación contextual con filtros de permisos.
- Memoria de conversaciones y decisiones aprobadas.

## Fase 3 — Integraciones iniciales

Objetivo: conectar el producto con herramientas empresariales comunes.

Entregables:

- Webhooks entrantes y salientes.
- Email básico.
- Calendario básico.
- CRM connector genérico.
- Tool Router con allowlist por tenant.
- Reintentos y dead-letter queues.

## Fase 4 — Monetización y administración

Objetivo: hacer el SaaS operable comercialmente.

Entregables:

- Planes y límites de uso.
- Medición de tokens, ejecuciones, almacenamiento y usuarios.
- Panel de administración de tenant.
- Exportación de auditoría.
- Políticas de retención.

## Fase 5 — Separación selectiva de servicios

Objetivo: extraer módulos con necesidad real de independencia.

Candidatos:

- Knowledge Service.
- Integration Service.
- Notification Service.
- Agent Orchestrator Service.
- Billing Service.

## Fase 6 — Enterprise readiness

Objetivo: vender a clientes con mayores requisitos de seguridad y operación.

Entregables:

- SSO/SAML/OIDC empresarial.
- SCIM.
- Auditoría inmutable.
- Data retention configurable.
- Reportes de seguridad.
- Ambientes por cliente si aplica.
- Backups y restore verificados.

## Riesgos a controlar

- Sobrediseñar microservicios antes de validar el producto.
- No aislar datos por tenant desde el inicio.
- Permitir ejecución autónoma sin controles.
- No auditar prompts, contexto y aprobaciones.
- Ignorar costos de IA y almacenamiento.
