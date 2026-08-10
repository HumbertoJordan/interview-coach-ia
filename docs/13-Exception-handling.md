1. ¿Qué es una excepción?

Una excepción representa una situación anormal durante la ejecución de un programa que interrumpe el flujo normal.

2. RuntimeException

Es una excepción que ocurre durante la ejecución y no obliga a declararla con throws.

Nuestra:

UserNotFoundException extends RuntimeException

es una excepción personalizada.

3. UserNotFoundException

La utilizamos cuando buscamos un usuario que no existe:

.orElseThrow(() ->
    new UserNotFoundException(
        "Usuario no encontrado con id: " + id
    )
);
4. @RestControllerAdvice

Permite centralizar el tratamiento de excepciones de los controllers.

En lugar de poner try/catch en cada endpoint:

Controller 1 ─┐
Controller 2 ─┼──→ GlobalExceptionHandler
Controller 3 ─┘
5. @ExceptionHandler

Indica qué excepción maneja cada método.

Ejemplo:

@ExceptionHandler(UserNotFoundException.class)

maneja específicamente UserNotFoundException.

6. ApiResponseErrorDto

Nuestra estructura estándar para errores:

success
message
errors

Donde errors permite devolver errores específicos de cada campo.

7. @Valid

Activa la validación del objeto recibido:

@RequestBody @Valid UserRequestDto userRequestDto

Spring analiza las restricciones:

@NotBlank
@Email
@Size

8. MethodArgumentNotValidException

Spring lanza esta excepción cuando un objeto recibido mediante @RequestBody no cumple las validaciones.

9. FieldError

Representa el error de un campo concreto.

Obtenemos los errores mediante:

exception.getBindingResult().getFieldErrors()

10. Collectors.toMap

Convertimos los FieldError en:

Map<String, String>

mediante:

.collect(Collectors.toMap(
    error -> error.getField(),
    error -> error.getDefaultMessage()
));
11. Códigos HTTP

En nuestro proyecto:

200 OK
    → operación exitosa

400 BAD REQUEST
    → datos enviados inválidos

404 NOT FOUND
    → recurso inexistente