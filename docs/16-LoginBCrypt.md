# Autenticación básica

## Objetivo

Implementar un sistema básico de autenticación mediante email y contraseña,
utilizando BCrypt para almacenar las contraseñas de forma segura.

## Flujo

El proceso de autenticación funciona de la siguiente manera:

1. El cliente envía email y contraseña.
2. `AuthController` recibe el `LoginRequestDto`.
3. `UserService` busca el usuario mediante `findByEmail()`.
4. `PasswordEncoder.matches()` compara la contraseña recibida con el hash almacenado.
5. Si las credenciales son correctas, se devuelve el usuario.
6. Si la contraseña es incorrecta, se lanza `InvalidCredentialException`.
7. `GlobalExceptionHandler` transforma la excepción en una respuesta HTTP 401.

## LoginRequestDto

Se creó `LoginRequestDto` para recibir las credenciales:

- `email`
- `password`

Se utilizan las validaciones:

- `@NotBlank`
- `@Email`
- `@Size`

El DTO evita recibir directamente la entidad `User` desde el cliente.

## UserRepository

Se agregó:

```java
Optional<User> findByEmail(String email);