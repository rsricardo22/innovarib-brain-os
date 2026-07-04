# Arquitectura

## Visión

Innovarib Brain OS opera como una capa de agentes de negocio sobre los sistemas existentes de una empresa. El MVP concentra capacidades en un backend modular para reducir complejidad operativa, pero cada módulo define límites claros para extraerse como microservicio cuando exista tracción.

## Principios

- Multi-tenant por diseño.
- IA con humano en el ciclo para autonomía nivel 2.
- Auditoría completa de prompts, decisiones, aprobaciones y herramientas.
- Separación lógica entre orquestación, memoria, aprobaciones y conectores.
- Evolución incremental hacia microservicios orientados por eventos.

## Módulos MVP

| Módulo | Responsabilidad |
| --- | --- |
| Identity & Tenant | tenants, usuarios, roles, permisos y contexto actual |
| Agent Orchestrator | recibe objetivos, crea borradores y coordina routers |
| Approval Workflow | estados draft/pending/approved/rejected |
| Memory/RAG | fuentes, chunks, embeddings y recuperación contextual |
| Model Router | selección de proveedor/modelo por coste, riesgo y tenant |
| Tool Router | catálogo y ejecución controlada de herramientas |
| Audit | bitácora inmutable de acciones relevantes |

## Despliegue local

Docker Compose levanta frontend, backend, PostgreSQL, Redis, RabbitMQ y MinIO. El backend expone API REST síncrona y publica eventos de dominio para procesos asíncronos.

## Camino a microservicios

1. Mantener módulos en paquetes separados.
2. Persistir eventos de dominio y contratos estables.
3. Extraer primero RAG/Memory y Tool Router si escalan de forma diferente.
4. Extraer Orchestrator cuando haya múltiples tipos de agentes.
