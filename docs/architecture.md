# Arquitectura inicial — Innovarib Brain / Business Agent OS

## Objetivo

Innovarib Brain / Business Agent OS es una plataforma SaaS multi-tenant para operar agentes empresariales que redactan, investigan, clasifican, recomiendan y ejecutan flujos bajo supervisión humana. El MVP prioriza una experiencia vendible: IA de nivel 2, donde el sistema propone acciones y contenido, y una persona aprueba antes de ejecutar cambios sensibles.

## Principios de arquitectura

1. **MVP modular, no microservicios prematuros**: iniciar con un backend modular en Spring Boot 3 que pueda desplegarse como monolito modular.
2. **Separación futura por bounded contexts**: cada módulo debe tener paquetes, tablas, eventos y APIs internas claras para extraerse luego como microservicio.
3. **Multi-tenant desde el día uno**: todas las entidades de negocio incluyen `tenant_id`; la autorización evalúa tenant, rol, permisos y ownership.
4. **Human-in-the-loop**: las acciones generadas por IA pasan por estados de revisión antes de publicarse, enviarse o ejecutarse.
5. **Event-driven donde aporte valor**: RabbitMQ se usa para tareas asíncronas, auditoría, indexación, notificaciones e integraciones.
6. **Persistencia poliglota controlada**: PostgreSQL como fuente de verdad, Redis para caché/locks/sesiones, MinIO para archivos y RabbitMQ para eventos.
7. **Seguridad y auditoría por defecto**: trazabilidad de prompts, respuestas, aprobaciones, acciones, integraciones y cambios de configuración.

## Stack base

- **Backend**: Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Flyway/Liquibase.
- **Frontend**: Angular 18/19, TypeScript, diseño por módulos funcionales.
- **Base de datos**: PostgreSQL.
- **Cache y coordinación**: Redis.
- **Mensajería**: RabbitMQ.
- **Object storage**: MinIO compatible con S3.
- **Contenedores**: Docker Compose para desarrollo y demo MVP.

## Vista lógica del MVP

```text
[Angular Web App]
        |
        v
[API Gateway interno / Backend Spring Boot modular]
        |
        +-- Identity & Tenant Module
        +-- Workspace & CRM Module
        +-- Agent Orchestration Module
        +-- Approval Workflow Module
        +-- Knowledge & Files Module
        +-- Integrations Module
        +-- Billing/Admin Module
        +-- Audit/Observability Module
        |
        +--> PostgreSQL
        +--> Redis
        +--> RabbitMQ
        +--> MinIO
```

## Módulos iniciales

### 1. Identity & Tenant

Responsable de tenants, usuarios, roles, permisos, membresías, invitaciones y políticas de acceso.

### 2. Workspace & Business Data

Gestiona espacios de trabajo, organizaciones cliente, contactos, oportunidades, tareas, notas y objetos operativos básicos para vender el MVP.

### 3. Agent Orchestration

Coordina agentes, prompts, plantillas, configuración de modelos, contexto de negocio, memoria operativa y generación de propuestas.

### 4. Human Approval Workflow

Gestiona borradores, revisiones, aprobaciones, rechazos, comentarios, historial y ejecución posterior a aprobación.

### 5. Knowledge & Files

Gestiona documentos, carga de archivos, metadatos, extracción de texto, almacenamiento en MinIO y preparación para RAG.

### 6. Integrations & Tooling

Abstrae conectores externos: email, calendario, CRM, WhatsApp, webhooks, herramientas internas y futuras automatizaciones.

### 7. Admin, Billing & Plans

Gestiona planes, límites, cuotas, uso, configuración de tenant y administración operativa.

### 8. Audit & Observability

Registra eventos de seguridad, acciones de usuario, acciones de agente, cambios de permisos, errores, métricas y trazas de negocio.

## Patrón de monolito modular

Cada módulo debe tener una estructura similar:

```text
backend/src/main/java/com/innovarib/brainos/<module>/
  api/              # Controllers y DTOs externos
  application/      # Casos de uso y servicios de aplicación
  domain/           # Entidades, reglas y puertos
  infrastructure/   # Repositorios, mensajería, clientes externos
```

Reglas:

- Un módulo no accede directamente a tablas internas de otro módulo.
- La comunicación síncrona entre módulos se realiza mediante servicios de aplicación o puertos internos.
- La comunicación asíncrona se realiza mediante eventos RabbitMQ.
- Cada tabla pertenece a un módulo propietario.
- Los DTOs externos no deben exponerse como entidades JPA.

## Flujo MVP principal

1. Usuario crea o selecciona un tenant/workspace.
2. Usuario carga información de negocio o crea un caso de trabajo.
3. Usuario solicita una propuesta a un agente.
4. El Agent Orchestration Module arma contexto con datos del tenant, documentos y memoria permitida.
5. La IA genera un borrador o recomendación.
6. El borrador queda en estado `PENDING_REVIEW`.
7. Un humano aprueba, edita o rechaza.
8. Si se aprueba, el sistema ejecuta la acción permitida o registra la decisión.
9. Se emiten eventos de auditoría, notificación e indexación.

## Separación futura a microservicios

El monolito modular debe prepararse para dividirse en estos servicios desplegables:

- Identity Service
- Tenant Service
- Agent Orchestrator Service
- Approval Service
- Knowledge Service
- Integration Service
- Notification Service
- Audit Service
- Billing Service
- Analytics Service

La extracción debe ocurrir solo cuando existan señales reales: escala independiente, equipos distintos, ciclos de release separados o requisitos de aislamiento.
