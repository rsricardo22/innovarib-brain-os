# Innovarib Brain OS

Innovarib Brain OS es un **Business Agent OS** multi-tenant para empresas que quieren operar agentes de IA con control humano. Esta primera versión prioriza un MVP vendible: el agente redacta propuestas, respuestas o tareas; un humano revisa y aprueba; y el sistema conserva auditoría, memoria y contexto por tenant.

## Alcance del MVP

- **Nivel 2 de autonomía:** la IA redacta, el humano aprueba antes de ejecutar o enviar.
- **Monorepo preparado para crecer:** backend Spring Boot modular y frontend Angular.
- **Infraestructura local:** PostgreSQL, Redis, RabbitMQ y MinIO con Docker Compose.
- **Multi-tenant desde el inicio:** todas las entidades principales incluyen `tenant_id`.
- **Seguridad base:** JWT/OIDC-ready, roles, permisos, auditoría y trazabilidad.

## Estructura

```text
apps/
  backend/     Spring Boot 3 + Java 17
  frontend/    Angular 18
infra/
  minio/       configuración futura de buckets/policies
docs/          arquitectura, contratos y estrategia técnica
```

## Requisitos

- Docker y Docker Compose v2
- Java 17 y Maven 3.9+ para desarrollo backend local
- Node.js 20+ y npm para desarrollo frontend local

## Ejecución rápida con Docker Compose

```bash
docker compose up --build
```

Servicios principales:

- Frontend: <http://localhost:4200>
- Backend API: <http://localhost:8080>
- PostgreSQL: `localhost:5432`
- RabbitMQ Management: <http://localhost:15672> (`guest` / `guest`)
- MinIO Console: <http://localhost:9001> (`minioadmin` / `minioadmin`)

## Desarrollo backend

```bash
cd apps/backend
mvn spring-boot:run
```

Endpoints iniciales:

- `GET /api/health`
- `GET /api/agents`
- `POST /api/drafts`
- `GET /api/drafts`
- `POST /api/drafts/{id}/approve`
- `POST /api/drafts/{id}/reject`

Para el MVP se usa `X-Tenant-Id` como cabecera obligatoria en endpoints de negocio. En producción debe sustituirse por tenant derivado de JWT/OIDC.

## Desarrollo frontend

```bash
cd apps/frontend
npm install
npm start
```

## Variables relevantes

| Variable | Descripción | Valor local |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC PostgreSQL | `jdbc:postgresql://postgres:5432/brainos` |
| `SPRING_DATASOURCE_USERNAME` | usuario DB | `brainos` |
| `SPRING_DATASOURCE_PASSWORD` | password DB | `brainos` |
| `SPRING_RABBITMQ_HOST` | host RabbitMQ | `rabbitmq` |
| `BRAINOS_SECURITY_DEV_MODE` | habilita seguridad simplificada local | `true` |

## Documentación

- [Arquitectura](docs/architecture.md)
- [Diseño de base de datos](docs/database-design.md)
- [Contratos API](docs/api-contracts.md)
- [Eventos RabbitMQ](docs/rabbitmq-events.md)
- [Seguridad multi-tenant](docs/security-multitenant.md)
- [Estrategia RAG y memoria](docs/rag-memory-strategy.md)
- [Model Router](docs/model-router.md)
- [Tool Router](docs/tool-router.md)
- [Roadmap](docs/roadmap.md)
