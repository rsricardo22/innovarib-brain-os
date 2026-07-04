# Tool Router

## Responsabilidad

Exponer herramientas internas de forma controlada para agentes.

## MVP

El MVP no ejecuta acciones externas automáticamente. Solo prepara acciones propuestas y requiere aprobación humana.

## Controles

- Catálogo de herramientas por tenant.
- Scopes por herramienta.
- Validación de entrada y salida.
- Circuit breakers y rate limits.
- Auditoría de cada intento.
