# Modelo del dominio

Entidades principales

User

Representa un usuario registrado.

Interview

Representa una entrevista.

Document

Representa cualquier archivo.

Analysis

Resultado generado por IA.

Dimension

Cada puntuación obtenida durante el análisis.

---

Relaciones

User

1 ------ N Interview

Interview

1 ------ N Document

Interview

1 ------ 1 Analysis

Analysis

1 ------ N Dimension