# Seguridad de contraseñas e integridad de datos

## 1. Objetivo

En esta unidad incorporamos dos mecanismos importantes para proteger los usuarios de la aplicación:

1. Hashing seguro de contraseñas mediante BCrypt.
2. Control de emails duplicados mediante una regla de negocio.

También analizamos cómo la aplicación y la base de datos pueden participar en la protección de la integridad de los datos.

---

# 2. Problema inicial: contraseñas en texto plano

Inicialmente las contraseñas se almacenaban directamente en la base de datos.

Ejemplo:

```text
password123
```

Esto es inseguro porque cualquier persona con acceso a la base de datos podría conocer las contraseñas.

La contraseña nunca debería almacenarse en texto plano.

---

# 3. Hashing vs. cifrado

BCrypt realiza **hashing**, no cifrado.

La diferencia fundamental es:

### Cifrado

El dato puede cifrarse y posteriormente descifrarse utilizando una clave.

```text
texto
   ↓
cifrado
   ↓
texto cifrado
   ↓
descifrado
   ↓
texto original
```

### Hashing

El dato se transforma en un valor que no se utiliza para recuperar el texto original.

```text
contraseña
     ↓
   BCrypt
     ↓
   hash
     ↓
base de datos
```

Por eso no debemos pensar en "desencriptar" una contraseña BCrypt.

Durante el login se comprueba si la contraseña proporcionada coincide con el hash almacenado.

---

# 4. Dependencia utilizada

Agregamos al `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

En esta etapa no agregamos todavía:

```text
spring-boot-starter-security
```

La intención fue aprender primero el funcionamiento del hashing de contraseñas antes de incorporar toda la infraestructura de Spring Security.

---

# 5. PasswordEncoder

Creamos una configuración para registrar un `PasswordEncoder` como Bean de Spring.

```java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

Esto permite que Spring administre el objeto y posteriormente lo inyecte en los componentes que lo necesiten.

---

# 6. Inyección de PasswordEncoder

Nuestro `UserService` ahora recibe dos dependencias:

```java
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
```

Como utilizamos:

```java
@RequiredArgsConstructor
```

Lombok genera automáticamente el constructor necesario.

El flujo queda:

```text
UserService
    │
    ├── UserRepository
    │
    └── PasswordEncoder
```

Esto mantiene el Service desacoplado de la implementación concreta de BCrypt.

El Service trabaja con la abstracción:

```java
PasswordEncoder
```

mientras que Spring proporciona:

```java
BCryptPasswordEncoder
```

---

# 7. Hashing durante la creación del usuario

Antes del `save()` hacemos:

```java
user.setPassword(
    passwordEncoder.encode(user.getPassword())
);
```

El flujo completo es:

```text
UserRequestDto
      ↓
UserMapper
      ↓
User
      ↓
PasswordEncoder.encode()
      ↓
BCrypt
      ↓
hash
      ↓
UserRepository.save()
      ↓
MySQL
```

La contraseña original nunca llega a almacenarse.

---

# 8. Comprobación en MySQL

Antes de incorporar BCrypt teníamos registros de prueba con contraseñas en texto plano.

Ejemplo:

```text
password123
```

Después de incorporar BCrypt aparecieron valores similares a:

```text
$2a$10$VUSTSKzVoKCRVcOHNALP2eSvQ95GMgD0Or5qLpKcncQjKVC/OjCf2
```

Esto confirmó que BCrypt estaba funcionando.

Los registros antiguos fueron conservados porque forman parte de las pruebas del proyecto.

---

# 9. BCrypt y el salt

BCrypt utiliza un salt.

Esto significa que una misma contraseña puede generar hashes diferentes.

Por ejemplo:

```text
MiPassword123
      ↓
BCrypt
      ↓
$2a$10$...
```

Si volvemos a utilizar:

```text
MiPassword123
```

podemos obtener otro hash diferente.

Esto es correcto y es una característica de seguridad.

Por lo tanto, no debemos comparar dos hashes BCrypt esperando que sean iguales simplemente porque se utilizó la misma contraseña.

La comprobación correcta se realizará mediante:

```java
passwordEncoder.matches(...)
```

Este concepto será utilizado posteriormente durante el login.

---

# 10. Actualización de contraseña

Inicialmente el método `updateUser()` tenía:

```java
existingUser.setPassword(
    userRequestDto.getPassword()
);
```

Eso habría vuelto a almacenar la contraseña en texto plano.

Lo corregimos utilizando:

```java
existingUser.setPassword(
    passwordEncoder.encode(
        userRequestDto.getPassword()
    )
);
```

Por lo tanto ahora tenemos:

```text
CREATE
password
   ↓
BCrypt
   ↓
hash
   ↓
BD
```

y:

```text
UPDATE
password nueva
   ↓
BCrypt
   ↓
hash
   ↓
BD
```

Ambas operaciones utilizan el mismo mecanismo de seguridad.

---

# 11. Regla de negocio: email único

También encontramos un problema real durante las pruebas:

```text
jordan@example.com
```

aparecía asociado a dos usuarios.

Para evitar que la aplicación permita registrar nuevamente un email existente utilizamos:

```java
existsByEmail(String email)
```

El flujo es:

```text
POST /api/users
       ↓
existsByEmail()
       ↓
¿Existe?
   ├── Sí → EmailAlreadyExistsException
   │
   └── No → continuar
```

---

# 12. EmailAlreadyExistsException

Creamos una excepción específica para representar este problema:

```text
EmailAlreadyExistsException
```

No utilizamos `UserNotFoundException` porque representan situaciones diferentes.

```text
UserNotFoundException
→ el usuario solicitado no existe.

EmailAlreadyExistsException
→ intentamos crear un usuario con un email existente.
```

---

# 13. HTTP 409 CONFLICT

Cuando el email ya existe, el `GlobalExceptionHandler` devuelve:

```text
HTTP 409 CONFLICT
```

La respuesta utiliza nuestro:

```text
ApiResponseErrorDto
```

Ejemplo:

```json
{
    "success": false,
    "message": "El correo electrónico ya está registrado",
    "errors": null
}
```

El código `409` representa un conflicto con el estado actual del recurso.

---

# 14. Responsabilidades de las capas

El control del email duplicado respeta nuestra arquitectura:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Base de datos
```

### Controller

Recibe la petición HTTP.

### Service

Aplica la regla de negocio:

```text
¿El email ya existe?
```

### Repository

Consulta la base de datos mediante:

```java
existsByEmail(email)
```

### Base de datos

Mantiene los datos persistidos.

---

# 15. Integridad de datos en dos niveles

La aplicación puede controlar que un email no se repita mediante:

```java
existsByEmail(email)
```

Pero idealmente la base de datos también debería tener una restricción:

```text
UNIQUE(email)
```

La arquitectura ideal sería:

```text
Aplicación
    ↓
existsByEmail()
    ↓
regla de negocio
    ↓
Base de datos
    ↓
UNIQUE(email)
```

De esta forma tenemos una protección en dos niveles.

---

# 16. Estado de UNIQUE(email)

En este proyecto todavía **no aplicamos la restricción `UNIQUE` en la base de datos**.

La razón es que actualmente existen datos de prueba duplicados:

```text
jordan@example.com
```

aparece en más de un registro.

Por lo tanto, antes de crear una restricción `UNIQUE` sería necesario limpiar o modificar esos datos.

Los registros duplicados se mantienen actualmente porque son datos utilizados durante las pruebas del proyecto.

---

# 17. Validación vs. regla de negocio vs. integridad

Es importante diferenciar tres conceptos.

### Validación

Comprueba que los datos recibidos tienen un formato correcto.

Ejemplo:

```java
@NotBlank
@Email
private String email;
```

Esto responde:

> ¿El email tiene un formato válido?

---

### Regla de negocio

Comprueba una condición propia de la aplicación.

Ejemplo:

```java
existsByEmail(email)
```

Esto responde:

> ¿Este email ya pertenece a otro usuario?

---

### Integridad de base de datos

Garantiza que los datos almacenados respeten determinadas restricciones.

Ejemplo futuro:

```text
UNIQUE(email)
```

Esto responde:

> ¿La base de datos permite almacenar este email nuevamente?

---

# 18. Arquitectura alcanzada

Después de esta unidad, el registro de usuarios funciona conceptualmente así:

```text
POST /api/users
       ↓
UserRequestDto
       ↓
Bean Validation
       ↓
UserMapper
       ↓
User
       ↓
UserService
       │
       ├── comprobar email
       │
       ├── BCrypt password
       │
       └── guardar usuario
       ↓
UserRepository
       ↓
MySQL
```

Si existe un problema:

```text
Exception
    ↓
GlobalExceptionHandler
    ↓
ApiResponseErrorDto
    ↓
HTTP correspondiente
```

---

# 19. Lo que aprendimos

En esta unidad aprendimos:

* Qué es hashing.
* Diferencia entre hashing y cifrado.
* Qué es BCrypt.
* Qué es `PasswordEncoder`.
* Cómo registrar un Bean con `@Bean`.
* Inyección de dependencias con Lombok.
* `passwordEncoder.encode()`.
* Por qué las contraseñas no deben almacenarse en texto plano.
* Qué es un salt.
* Por qué dos hashes BCrypt pueden ser diferentes para la misma contraseña.
* Cómo proteger también la actualización de contraseñas.
* Qué es una regla de negocio.
* `existsByEmail()`.
* `EmailAlreadyExistsException`.
* HTTP `409 CONFLICT`.
* Diferencia entre validación, regla de negocio e integridad de base de datos.

---

# 20. Próxima unidad

La próxima etapa será **autenticación**.

El primer concepto será:

```java
passwordEncoder.matches(...)
```

Aprenderemos cómo comprobar una contraseña contra el hash almacenado sin intentar recuperar la contraseña original.

Después construiremos progresivamente:

```text
Login
  ↓
PasswordEncoder.matches()
  ↓
Spring Security
  ↓
Autenticación
  ↓
JWT
  ↓
Autorización
  ↓
Roles y permisos
```

No incorporaremos todo de golpe. Cada componente se agregará cuando entendamos qué problema resuelve.
