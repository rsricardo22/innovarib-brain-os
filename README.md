
Lee el README.md de este repositorio.

Con base en la visión de Innovarib Brain / Business Agent OS, genera la primera versión del proyecto.

Prioriza un MVP funcional y vendible, no una arquitectura gigante imposible de ejecutar.

Entrega en el repositorio:

1. /docs/architecture.md
2. /docs/database-design.md
3. /docs/api-contracts.md
4. /docs/rabbitmq-events.md
5. /docs/security-multitenant.md
6. /docs/rag-memory-strategy.md
7. /docs/model-router.md
8. /docs/tool-router.md
9. /docs/roadmap.md
10. docker-compose.yml inicial
11. estructura base de monorepo
12. backend Spring Boot inicial
13. frontend Angular inicial
14. README con instrucciones de ejecución

Reglas:

- Usar Java 17, Spring Boot 3, Angular 18/19, PostgreSQL, Redis, RabbitMQ y MinIO.
- Diseñar multi-tenant desde el inicio.
- MVP nivel 2: IA redacta y humano aprueba.
- No implementar todos los microservicios todavía; agrupar por módulos para el MVP.
- Crear una arquitectura preparada para separar microservicios después.
- Incluir buenas prácticas de seguridad, auditoría y permisos.
- Crear código inicial ejecutable con Docker Compose.