# Reglas de negocio e integridad de datos

## 1. ¿Qué es una regla de negocio?

Una regla de negocio es una condición que debe cumplirse para que una operación sea válida dentro de la aplicación.

En nuestro proyecto tenemos, por ejemplo:

> Un usuario no puede registrarse utilizando un email que ya pertenece a otro usuario.

Esta regla no pertenece al Controller ni al Repository. La decisión de negocio se realiza en el Service.

---

## 2. ¿Por qué utilizamos `existsByEmail()`?

En `UserRepository` agregamos:

```java
boolean existsByEmail(String email);
```

Spring Data JPA interpreta el nombre del método y genera la consulta necesaria para comprobar si existe un usuario con ese email.

El resultado es:

* `true`: el email ya existe.
* `false`: el email no existe.

El Repository se encarga de consultar los datos.

El Service utiliza esa consulta para tomar una decisión de negocio.

---

## 3. Responsabilidad de cada capa

Nuestro flujo queda:

Controller → Service → Repository → Base de datos

### Controller

Recibe la petición HTTP y devuelve la respuesta HTTP.

### Service

Contiene la lógica de negocio.

En nuestro caso:

```text
¿Existe el email?
        ↓
      SÍ → lanzar excepción
      NO → guardar usuario
```

### Repository

Se comunica con la base de datos y proporciona operaciones de persistencia.

---

## 4. `EmailAlreadyExistsException`

Creamos una excepción específica:

```java
public class EmailAlreadyExistsException extends RuntimeException {
}
```

La utilizamos porque un email duplicado es diferente de un usuario inexistente.

Tenemos:

```text
UserNotFoundException
→ el recurso solicitado no existe.

EmailAlreadyExistsException
→ queremos crear un recurso que entra en conflicto con uno existente.
```

---

## 5. ¿Por qué HTTP 409?

Cuando intentamos registrar un email que ya existe, utilizamos:

```text
HTTP 409 CONFLICT
```

La solicitud tiene un formato válido y los datos cumplen las validaciones, pero existe un conflicto con el estado actual de los recursos.

Ejemplo:

```text
POST /api/users

email = jordan@example.com
```

Si `jordan@example.com` ya existe:

```text
409 CONFLICT
```

---

## 6. Diferencia entre 400, 404 y 409

### 400 Bad Request

La petición contiene datos inválidos.

Ejemplo:

```text
email = "abc"
password = "123"
```

La validación de Bean Validation detecta el problema.

---

### 404 Not Found

El recurso solicitado no existe.

Ejemplo:

```text
GET /api/users/99
```

Si el usuario 99 no existe:

```text
404 NOT FOUND
```

---

### 409 Conflict

La petición es válida, pero entra en conflicto con los datos existentes.

Ejemplo:

```text
POST /api/users
email = "jordan@example.com"
```

Si ese email ya existe:

```text
409 CONFLICT
```

---

## 7. `GlobalExceptionHandler`

Nuestro `GlobalExceptionHandler` centraliza el tratamiento de excepciones.

Esto evita tener que escribir un `try/catch` diferente en cada Controller.

Por ejemplo:

```text
EmailAlreadyExistsException
        ↓
GlobalExceptionHandler
        ↓
HTTP 409
        ↓
ApiResponseErrorDto
```

La respuesta puede ser:

```json
{
    "success": false,
    "message": "El correo electrónico ya está registrado",
    "errors": null
}
```

---

## 8. Validación de aplicación e integridad de base de datos

La comprobación mediante:

```java
existsByEmail(email)
```

protege la lógica de la aplicación.

Pero posteriormente también debemos establecer una restricción `UNIQUE` en la columna `email` de la base de datos.

Esto proporciona una segunda capa de protección.

La arquitectura será:

```text
Aplicación
    ↓
existsByEmail()
    ↓
Regla de negocio
    ↓
Base de datos
    ↓
UNIQUE(email)
```

La aplicación controla el comportamiento esperado y la base de datos garantiza la integridad de los datos.

---

# 9. Conclusión

Aprendimos que:

* El Repository consulta.
* El Service aplica las reglas de negocio.
* El Controller maneja HTTP.
* Las excepciones representan situaciones concretas.
* `GlobalExceptionHandler` transforma excepciones en respuestas HTTP.
* `400` representa datos inválidos.
* `404` representa un recurso inexistente.
* `409` representa un conflicto con el estado actual.
* Una regla importante debe estar protegida tanto por la aplicación como por la base de datos.

Este patrón será reutilizado posteriormente en autenticación, autorización y otras reglas del sistema.
