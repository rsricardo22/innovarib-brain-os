# Mapa de servicios y agrupación de los 44 microservicios

## Estrategia

La visión completa puede contener 44 microservicios, pero el MVP debe agruparlos en módulos iniciales dentro de un monolito modular. Esta decisión reduce complejidad operativa y mantiene la capacidad de separar servicios después.

## Agrupación inicial

| Grupo MVP | Microservicios/capacidades agrupadas | Motivo |
| --- | --- | --- |
| Identity & Tenant | 1 Identity, 2 Tenant, 3 Users, 4 Roles, 5 Permissions | Seguridad y aislamiento como base común. |
| Workspace & CRM | 6 Workspaces, 7 Accounts, 8 Contacts, 9 Deals, 10 Tasks, 11 Notes | Datos empresariales mínimos para operar agentes vendibles. |
| Agent Orchestration | 12 Agent Registry, 13 Prompt Manager, 14 Model Router, 15 Context Builder, 16 Agent Runs, 17 Memory Coordinator | Núcleo de IA; requiere iteración rápida en MVP. |
| Human Approval | 18 Drafts, 19 Reviews, 20 Approvals, 21 Policy Checks, 22 Action Executor | Implementa IA nivel 2 con control humano. |
| Knowledge & Files | 23 File Storage, 24 Document Parser, 25 Knowledge Base, 26 Embeddings Queue, 27 RAG Retrieval | Base para memoria y RAG sin sobrediseñar. |
| Integrations & Tools | 28 Email Connector, 29 Calendar Connector, 30 CRM Connector, 31 Webhooks, 32 Tool Router, 33 External API Proxy | Encapsula conectores y prepara automatizaciones. |
| Notifications | 34 In-app Notifications, 35 Email Notifications, 36 Approval Reminders | Puede iniciar como submódulo y extraerse pronto. |
| Audit & Compliance | 37 Audit Log, 38 Security Events, 39 Consent/Policy Records, 40 Data Retention | Trazabilidad y confianza empresarial. |
| Admin & Billing | 41 Plans, 42 Usage Metering, 43 Billing, 44 Tenant Settings | Necesario para comercialización y límites. |

## Módulos a implementar primero

1. Identity & Tenant.
2. Workspace & CRM básico.
3. Agent Orchestration mínimo.
4. Human Approval.
5. Knowledge & Files.
6. Audit.

## Módulos post-MVP temprano

1. Integrations & Tools.
2. Notifications.
3. Admin & Billing avanzado.
4. Analytics.

## Reglas para futura extracción

Un módulo puede convertirse en microservicio cuando cumpla al menos dos condiciones:

- Tiene carga de trabajo independiente.
- Necesita escalar de forma distinta.
- Tiene dependencias externas riesgosas o lentas.
- Requiere despliegues frecuentes sin afectar el core.
- Tiene datos con requisitos de seguridad o retención distintos.
- Puede comunicarse mediante APIs/eventos sin joins transaccionales con otros módulos.

## Orden probable de extracción

1. **Knowledge Service**: parsing, embeddings y RAG suelen requerir workers y recursos propios.
2. **Integration Service**: conectores externos fallan y evolucionan a ritmos distintos.
3. **Notification Service**: colas y reintentos independientes.
4. **Agent Orchestrator Service**: cuando aumente el volumen de ejecuciones.
5. **Billing Service**: cuando la monetización requiera proveedor de pagos y conciliación.
6. **Audit Service**: si compliance exige almacenamiento separado o inmutable.

## Contrato entre módulos

Cada módulo debe publicar:

- APIs REST para operaciones de usuario.
- Eventos de dominio para cambios relevantes.
- DTOs/versiones estables.
- Migraciones de tablas propias.
- Métricas y logs con `tenant_id`, `correlation_id` y `actor_id`.
