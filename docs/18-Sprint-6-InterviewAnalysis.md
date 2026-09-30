# Sprint 6 — InterviewAnalysis

## Objetivo

Implementar la capa de análisis de una entrevista, dejando preparada la arquitectura para una futura integración con inteligencia artificial.

## Implementación

Se creó la entidad `InterviewAnalysis` con una relación `OneToOne` con `Interview`.

Cada entrevista puede tener un único análisis.

Campos principales:

- `id`
- `interview`
- `summary`
- `strengths`
- `weaknesses`
- `recommendations`
- `createdAt`
- `updatedAt`

## Backend implementado

### Entity

`InterviewAnalysis`

Relación:


Interview 1 ─────── 1 InterviewAnalysis 