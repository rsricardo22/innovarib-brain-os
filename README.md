# Innovarib Brain OS

Innovarib Brain OS es la base de un **Business Agent OS** multi-tenant para operar flujos de negocio con IA, memoria, herramientas, eventos y aprobación humana. El MVP apunta a un nivel 2: la IA redacta o prepara acciones y una persona aprueba antes de ejecutar.

## Stack inicial

- Java 17 / Spring Boot 3 para el backend.
- Angular 18/19 para el frontend.
- PostgreSQL para datos transaccionales multi-tenant.
- Redis para caché, sesiones efímeras y coordinación liviana.
- RabbitMQ para eventos asíncronos.
- MinIO para almacenamiento compatible con S3.
- Nginx como proxy opcional para desarrollo local.

## Ejecución local con Docker Compose

### 1. Preparar variables de entorno

Copia el archivo de ejemplo y ajusta secretos o puertos si lo necesitas:

```bash
cp .env.example .env
```

> Los valores incluidos son únicamente para desarrollo local. Cambia las contraseñas antes de usar cualquier ambiente compartido.

### 2. Levantar la plataforma

```bash
docker compose up -d
```

Este comando levanta PostgreSQL, Redis, RabbitMQ, MinIO, un contenedor base para el backend Spring Boot y un contenedor base para el frontend Angular.

### 3. Verificar servicios

```bash
docker compose ps
```

URLs locales por defecto:

| Servicio | URL / puerto | Credenciales por defecto |
| --- | --- | --- |
| Backend base | <http://localhost:8080> | N/A |
| Frontend base | <http://localhost:4200> | N/A |
| PostgreSQL | `localhost:5432` | `innovarib` / `innovarib_dev_password` |
| Redis | `localhost:6379` | N/A |
| RabbitMQ AMQP | `localhost:5672` | `innovarib` / `innovarib_dev_password` |
| RabbitMQ Management | <http://localhost:15672> | `innovarib` / `innovarib_dev_password` |
| MinIO API | <http://localhost:9000> | `minioadmin` / `minioadmin_dev_password` |
| MinIO Console | <http://localhost:9001> | `minioadmin` / `minioadmin_dev_password` |

### 4. Levantar Nginx opcional

Nginx está detrás del perfil `proxy` para no ocupar el puerto 80 por defecto:

```bash
docker compose --profile proxy up -d nginx
```

Con el perfil activo, Nginx expone:

- `/api/` hacia el backend.
- `/` hacia el frontend.

### 5. Detener y limpiar

Detener contenedores conservando volúmenes:

```bash
docker compose down
```

Eliminar también datos locales persistidos:

```bash
docker compose down -v
```

## Notas de implementación

- Los servicios `backend` y `frontend` son contenedores base para validar la composición inicial. En las siguientes tareas deben reemplazarse por los Dockerfiles reales del backend Spring Boot y frontend Angular.
- Las dependencias internas entre servicios usan nombres DNS de Docker Compose: `postgres`, `redis`, `rabbitmq` y `minio`.
- La configuración está centralizada en `.env.example` para facilitar onboarding y despliegues locales reproducibles.
