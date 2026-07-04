# Alcance del MVP — Innovarib Brain / Business Agent OS

## Objetivo del MVP

Construir una primera versión funcional y vendible de Innovarib Brain que permita a empresas usar agentes de IA para redactar propuestas, organizar conocimiento, asistir procesos comerciales y ejecutar acciones únicamente después de aprobación humana.

## Posicionamiento

El MVP no intenta entregar los 44 microservicios como servicios independientes. En su lugar, agrupa capacidades en módulos de producto que resuelven un flujo completo:

- Configurar empresa y usuarios.
- Cargar conocimiento básico.
- Pedir asistencia a un agente.
- Recibir borradores o recomendaciones.
- Aprobar o rechazar acciones.
- Auditar lo ocurrido.

## Nivel de autonomía

El MVP implementa **IA nivel 2**:

- La IA redacta, clasifica, resume y recomienda.
- La IA no ejecuta acciones críticas sin aprobación humana.
- Toda salida relevante se guarda como borrador revisable.
- Las aprobaciones quedan auditadas.

## Funcionalidades incluidas

### Multi-tenant básico

- Crear tenants.
- Gestionar usuarios por tenant.
- Roles iniciales: `OWNER`, `ADMIN`, `MEMBER`, `VIEWER`.
- Aislamiento por `tenant_id` en tablas de negocio.

### Workspace empresarial

- Espacios de trabajo por tenant.
- Contactos y cuentas simples.
- Tareas y notas.
- Objetos suficientes para casos comerciales iniciales.

### Agentes IA

- Catálogo inicial de agentes.
- Plantillas de prompt.
- Generación de borradores.
- Registro de contexto usado.
- Estados de ejecución: `QUEUED`, `RUNNING`, `DRAFT_CREATED`, `FAILED`.

### Revisión humana

- Bandeja de aprobaciones.
- Editar borradores antes de aprobar.
- Aprobar, rechazar o pedir cambios.
- Historial de decisiones.

### Conocimiento y archivos

- Subida de archivos a MinIO.
- Metadatos en PostgreSQL.
- Extracción de texto como tarea asíncrona.
- Preparación para embeddings/RAG sin bloquear el MVP.

### Eventos y auditoría

- Eventos RabbitMQ para generación, aprobación, notificación e indexación.
- Bitácora de auditoría por tenant.
- Registro de actor humano o agente.

## Funcionalidades fuera del MVP

- 44 microservicios desplegados por separado.
- Automatización autónoma sin revisión humana.
- Marketplace público de agentes.
- Facturación compleja con impuestos por país.
- Entrenamiento propio de modelos.
- Integraciones profundas con todos los CRMs.
- Alta disponibilidad multi-región.
- Data warehouse avanzado.

## Entregables técnicos mínimos

- Monorepo preparado para backend y frontend.
- Backend Spring Boot 3 con arquitectura modular.
- Frontend Angular con navegación base.
- Docker Compose con PostgreSQL, Redis, RabbitMQ y MinIO.
- Migraciones de base de datos.
- Documentación de arquitectura, seguridad, eventos, roadmap y diseño de datos.

## Criterio de vendibilidad

El MVP se considera vendible si permite demostrar a un cliente:

1. Separación de datos por empresa.
2. Carga de conocimiento empresarial.
3. Generación de un borrador útil por IA.
4. Revisión humana trazable.
5. Ejecución o registro de una acción aprobada.
6. Auditoría básica para confianza y compliance.
