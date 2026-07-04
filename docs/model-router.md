# Model Router

## Responsabilidad

Seleccionar el modelo adecuado según tenant, caso de uso, sensibilidad, coste, latencia y disponibilidad.

## Política MVP

- Redacción comercial: modelo económico y rápido.
- Documentos legales/financieros: modelo de mayor razonamiento y aprobación obligatoria.
- Datos sensibles: proveedores permitidos por configuración del tenant.

## Registro de invocación

Cada llamada debe guardar modelo, proveedor, tokens estimados, coste estimado, latencia, tenant, usuario y correlation id.
