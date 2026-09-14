# Sprint 5 — Entidad Interview

## Objetivo

Implementar y documentar la entidad `Interview`, que representa una entrevista realizada dentro de la plataforma Interview Coach IA.

La entidad permite relacionar una entrevista con:

- Un usuario propietario.
- Múltiples documentos asociados.
- Un estado de la entrevista.
- Información básica de identificación.
- Fechas de creación y actualización.

---

# 1. Entidad Interview

La clase `Interview` representa una entrevista dentro del sistema.

Ubicación:


src/main/java/com/interviewcoach/entity/Interview.java

| Campo     | Tipo            | Descripción                          |
| --------- | --------------- | ------------------------------------ |
| id        | Long            | Identificador único de la entrevista |
| title     | String          | Título de la entrevista              |
| date      | LocalDate       | Fecha de la entrevista               |
| type      | String          | Tipo de entrevista                   |
| status    | InterviewStatus | Estado actual                        |
| createdAt | LocalDateTime   | Fecha y hora de creación             |
| updatedAt | LocalDateTime   | Fecha y hora de última actualización |
| user      | User            | Usuario propietario                  |
| documents | List<Document>  | Documentos asociados                 |

2. Identificador

La entidad utiliza un identificador generado automáticamente:

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

La estrategia IDENTITY delega la generación del identificador a la base de datos.

En MySQL esto se corresponde con una columna AUTO_INCREMENT.

3. Información básica

La entrevista posee tres datos principales:

private String title;

private LocalDate date;

private String type;
title

Representa el nombre o título de la entrevista.

Ejemplo:

Entrevista Backend Java
date

Representa la fecha en la que se realizó o está prevista la entrevista.

Se utiliza:

LocalDate

porque solamente interesa la fecha y no la hora.

type

Permite identificar el tipo de entrevista.

Ejemplos posibles:

SCREENING
TECHNICAL
FINAL

El campo se mantiene como String para permitir ampliar los tipos posteriormente.

4. Estado de la entrevista

El estado se representa mediante el enum:

InterviewStatus

Ubicación:

src/main/java/com/interviewcoach/entity/InterviewStatus.java

Actualmente posee los siguientes valores:

public enum InterviewStatus {

    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED

}

La entidad utiliza:

@Enumerated(EnumType.STRING)
private InterviewStatus status;

Se utiliza EnumType.STRING para almacenar en la base de datos el nombre del estado y no su posición numérica.

Ejemplo:

PENDING
IN_PROGRESS
COMPLETED
CANCELLED

Esto evita problemas si posteriormente se modifica el orden de los valores del enum.

5. Fechas automáticas

La entidad posee:

private LocalDateTime createdAt;

private LocalDateTime updatedAt;

Estas fechas son administradas automáticamente mediante callbacks de JPA.

Creación
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

Cuando una entrevista se guarda por primera vez:

createdAt = fecha actual
updatedAt = fecha actual
Actualización
@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}

Cada vez que la entidad se modifica, updatedAt se actualiza automáticamente.

6. Relación User → Interview

Una entrevista pertenece a un usuario.

La relación se implementa mediante:

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;

La relación conceptual es:

User 1 -------- N Interview

Un usuario puede tener múltiples entrevistas.

Cada entrevista pertenece obligatoriamente a un usuario.

La columna utilizada en la tabla interviews es:

user_id

El campo es obligatorio:

nullable = false

Esto evita que una entrevista quede almacenada sin propietario.

7. Relación Interview → Document

Una entrevista puede tener múltiples documentos asociados.

La relación se implementa mediante:

@OneToMany(
        mappedBy = "interview",
        cascade = CascadeType.ALL,
        orphanRemoval = true
)
private List<Document> documents = new ArrayList<>();

La relación conceptual es:

Interview 1 -------- N Document

Una entrevista puede tener documentos como:

CV
Job Description
Transcripción
Otros archivos relacionados con la entrevista
8. CascadeType.ALL

La relación utiliza:

cascade = CascadeType.ALL

Esto permite propagar las operaciones de persistencia desde Interview hacia sus documentos asociados.

Por ejemplo, las operaciones de persistencia pueden propagarse cuando corresponde.

9. orphanRemoval

También se utiliza:

orphanRemoval = true

Esto permite eliminar de la base de datos los documentos que dejan de pertenecer a la colección de la entrevista.

La intención es mantener consistente la relación entre:

Interview
    ↓
Documents
10. FetchType.LAZY

La relación con User utiliza:

fetch = FetchType.LAZY

Esto evita cargar automáticamente toda la información del usuario cada vez que se consulta una entrevista.

La información relacionada se obtiene cuando realmente es necesaria.

Esto ayuda a mantener un comportamiento más eficiente en consultas que trabajan con múltiples entrevistas.

11. Modelo de dominio

La estructura actual del dominio relacionada con entrevistas es:

                    ┌──────────────┐
                    │     User     │
                    └──────┬───────┘
                           │
                           │ 1
                           │
                           │ N
                    ┌──────▼───────┐
                    │   Interview  │
                    └──────┬───────┘
                           │
                           │ 1
                           │
                           │ N
                    ┌──────▼───────┐
                    │   Document   │
                    └──────────────┘
12. Persistencia

La entidad utiliza:

@Entity
@Table(name = "interviews")

Por lo tanto, Hibernate realiza el mapeo entre:

Clase Java
    ↓
Interview

y:

Tabla MySQL
    ↓
interviews
13. Arquitectura

La entidad forma parte de la capa de dominio/persistencia.

La utilización dentro de la aplicación sigue el flujo:

Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
MySQL

Para las respuestas HTTP se utilizan DTOs y mappers, evitando exponer directamente la entidad como contrato principal de la API.

14. DTO de creación

La creación de una entrevista se realiza mediante:

InterviewRequestDto

El controller recibe el DTO:

@RequestBody @Valid InterviewRequestDto requestDto

Luego el servicio crea la entrevista asociándola al usuario correspondiente.

15. DTO de respuesta

Las respuestas utilizan:

InterviewResponseDto

La conversión se realiza mediante:

InterviewMapper

Flujo:

Request DTO
    ↓
Service
    ↓
Interview Entity
    ↓
InterviewMapper
    ↓
Response DTO
16. Endpoints disponibles

El controller de entrevistas utiliza la ruta base:

/api/users
Crear entrevista
POST /api/users/{userId}/interviews

Ejemplo:

POST /api/users/7/interviews
Obtener entrevistas de un usuario
GET /api/users/{userId}/interviews

Ejemplo:

GET /api/users/7/interviews
Obtener una entrevista
GET /api/users/{userId}/interviews/{interviewId}

Ejemplo:

GET /api/users/7/interviews/2
Eliminar entrevista
DELETE /api/users/{userId}/interviews/{interviewId}

Ejemplo:

DELETE /api/users/7/interviews/2
17. Seguridad

Los endpoints de la aplicación se encuentran protegidos mediante Spring Security y JWT.

La autenticación funciona mediante:

Authorization: Bearer <JWT>

Los endpoints:

/api/**

requieren autenticación, excepto:

/api/auth/**

que corresponde a los endpoints públicos de autenticación.

18. Flujo de autenticación

El flujo actual es:

POST /api/auth/login
        ↓
Validación de email/password
        ↓
BCrypt
        ↓
JwtService
        ↓
Generación del JWT
        ↓
Cliente recibe token
        ↓
Authorization: Bearer JWT
        ↓
JwtAuthenticationFilter
        ↓
CustomUserDetailsService
        ↓
SecurityContext
        ↓
Acceso al endpoint protegido
19. Validación realizada

La implementación fue probada mediante Postman.

Login
POST /api/auth/login

Resultado:

200 OK

Se obtiene un JWT válido.

Endpoint protegido sin token
GET /api/users/14

Resultado:

403 Forbidden

Esto confirma que el endpoint está protegido.

Endpoint protegido con JWT
GET /api/users/14
Authorization: Bearer <JWT>

Resultado:

200 OK

Esto confirma que Spring Security reconoce y valida correctamente el JWT.