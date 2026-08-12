# Mapper — Conversión entre DTOs y Entidades

## 1. ¿Qué es un Mapper?

Un Mapper es una clase cuya responsabilidad es convertir objetos de un tipo a otro.

En nuestra aplicación vamos a utilizarlo principalmente para convertir:

    UserRequestDto → User

y:

    User → UserResponseDto

Esto permite mantener separadas las responsabilidades de cada objeto.

---

## 2. ¿Por qué necesitamos un Mapper?

En una API REST tenemos diferentes tipos de objetos.

### DTO de entrada

UserRequestDto representa los datos que el cliente puede enviar.

Por ejemplo:

    firstName
    lastName
    email
    password

### Entity

User representa el objeto que JPA utiliza para trabajar con la base de datos.

### DTO de salida

UserResponseDto representa los datos que queremos devolver al cliente.

No necesariamente queremos devolver todos los campos de User.

Por ejemplo, la contraseña nunca debería formar parte de la respuesta.

---

## 3. Flujo de entrada

Cuando un cliente crea un usuario:

    HTTP Request
         ↓
    UserRequestDto
         ↓
    UserMapper
         ↓
    User
         ↓
    UserService
         ↓
    UserRepository
         ↓
    MySQL

El Mapper convierte el DTO recibido en una Entity.

---

## 4. Flujo de salida

Después de guardar el usuario:

    MySQL
      ↓
    User
      ↓
    UserMapper
      ↓
    UserResponseDto
      ↓
    ApiResponseSuccessDto
      ↓
    ResponseEntity
      ↓
    HTTP Response

El Mapper convierte la Entity en un DTO de respuesta.

---

## 5. Ejemplo de nuestro proyecto

Podemos tener una clase:

    UserMapper

con dos métodos principales:

    fromDto(UserRequestDto dto)

y:

    toDto(User user)

Conceptualmente:

    fromDto()
        UserRequestDto → User

    toDto()
        User → UserResponseDto

---

## 6. Ejemplo tomado de SMédico

En el proyecto SMédico utilizamos:

    EncuestaMapper

El método:

    fromDto(EncuestaRequestDto dto)

convierte un EncuestaRequestDto en una entidad Encuesta.

El método:

    toDto(Encuesta encuesta)

convierte una entidad Encuesta en un EncuestaResponseDto.

Por lo tanto, el Mapper funciona como una capa de transformación entre DTOs y entidades.

---

## 7. ¿Dónde está ubicado?

El Mapper pertenece a su propio package:

    com.interviewcoach.mapper

Por lo tanto nuestra estructura será:

    com.interviewcoach
    │
    ├── controller
    ├── dto
    ├── entity
    ├── mapper
    ├── repository
    └── service

---

## 8. ¿El Mapper guarda información?

No.

El Mapper no debería encargarse de guardar datos en MySQL.

Su responsabilidad principal es transformar objetos.

El encargado de persistir los datos es:

    UserRepository

Y el Service coordina la lógica de negocio.

---

## 9. Responsabilidad de cada capa

### Controller

Se ocupa de HTTP:

    Request
    Response
    ResponseEntity

### DTO

Representa los datos que entran o salen de la API.

### Mapper

Convierte:

    DTO ↔ Entity

### Service

Contiene la lógica de negocio y coordina las operaciones.

### Repository

Se comunica con la base de datos mediante Spring Data JPA.

### Entity

Representa los datos persistidos en la base de datos.

---

## 10. Regla importante

No debemos mezclar las responsabilidades.

El Mapper no debería guardar directamente en la base de datos.

El Repository no debería construir respuestas HTTP.

El Controller no debería contener toda la lógica de negocio.

El DTO no debería ser utilizado como Entity de JPA.

Cada capa tiene una responsabilidad determinada.

---

## 11. Resumen

Mapper:

    DTO → Entity
    Entity → DTO

Service:

    lógica de negocio

Repository:

    persistencia

Controller:

    HTTP

Esta separación hace que el código sea más fácil de mantener, probar y ampliar.