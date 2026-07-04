# Estrategia RAG y memoria

## Objetivo

Dar contexto empresarial a los agentes sin mezclar datos entre tenants.

## Capas

1. Memoria de sesión: Redis con TTL corto.
2. Memoria operativa: PostgreSQL para drafts, decisiones y preferencias.
3. Memoria documental: MinIO para archivos y PostgreSQL/pgvector para chunks.

## Pipeline

1. Ingesta de fuente.
2. Extracción de texto.
3. Chunking por tamaño y semántica.
4. Embeddings por tenant.
5. Recuperación filtrada por `tenant_id`, permisos y tipo de documento.
6. Citación de fuentes en respuestas de IA.
