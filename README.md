# Innovarib Brain OS

Monorepo base para **Innovarib Brain / Business Agent OS**, orientado a un MVP multi-tenant donde la IA ayuda a redactar y ejecutar tareas bajo aprobación humana.

## Estructura del repositorio

```text
/backend
  /auth-service
  /tenant-service
  /brain-service
  /document-service
  /crm-service
  /campaign-service
  /agent-service
  /audit-service
  /notification-service
/frontend
/docs
/infra
/scripts
```

## Backend

La carpeta `backend` contiene módulos preparados para evolucionar a servicios independientes cuando el MVP lo requiera:

- `auth-service`: autenticación, autorización y sesiones.
- `tenant-service`: gestión multi-tenant, organizaciones y configuración por cliente.
- `brain-service`: orquestación de IA, flujos de aprobación humana y memoria/RAG.
- `document-service`: ingestión, almacenamiento y procesamiento de documentos.
- `crm-service`: contactos, empresas, oportunidades y actividad comercial.
- `campaign-service`: campañas, mensajes y seguimiento.
- `agent-service`: definición y ejecución controlada de agentes/herramientas.
- `audit-service`: trazabilidad, logs de seguridad y eventos de cumplimiento.
- `notification-service`: notificaciones internas y externas.

> Nota: todavía no se implementa lógica de negocio avanzada. Esta tarea solo establece la estructura base del monorepo.

## Frontend

La carpeta `frontend` queda reservada para la aplicación web Angular del producto.

## Documentación

La carpeta `docs` centralizará decisiones de arquitectura, contratos de API, seguridad multi-tenant, estrategia RAG/memoria y roadmap.

## Infraestructura

La carpeta `infra` almacenará manifiestos, configuración de despliegue, archivos Docker/Kubernetes/Terraform y otros recursos operativos.

## Scripts

La carpeta `scripts` almacenará utilidades de desarrollo, automatización local, migraciones auxiliares y tareas CI/CD.

## Requisitos previstos

- Java 17
- Spring Boot 3
- Node.js LTS
- Angular 18/19
- PostgreSQL
- Redis
- RabbitMQ
- MinIO
- Docker y Docker Compose

## Ejecución local

En esta fase inicial no hay servicios ejecutables todavía. Una vez agregados los proyectos backend/frontend y `docker-compose.yml`, la ejecución local esperada será:

```bash
docker compose up --build
```

Para validar la estructura actual:

```bash
find backend frontend docs infra scripts -maxdepth 2 -type d | sort
```

## Estado actual

- Monorepo inicial creado.
- Módulos backend definidos como carpetas base.
- Carpetas `frontend`, `docs`, `infra` y `scripts` preparadas.
- Sin lógica avanzada ni servicios productivos implementados todavía.
