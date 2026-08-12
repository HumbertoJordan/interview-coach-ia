Jakarta Bean Validation
1. ¿Qué es Validation?

Jakarta Bean Validation es una especificación que permite definir reglas de validación sobre los datos de una aplicación Java.

Por ejemplo, podemos establecer que:

un nombre no puede estar vacío;
un email debe tener formato válido;
una contraseña debe tener una longitud mínima;
un número debe estar dentro de determinado rango.

En nuestro proyecto utilizaremos la implementación integrada con Spring Boot para validar los datos que llegan a nuestra API.

2. ¿Qué problema resuelve?

Imaginemos que nuestro endpoint recibe:

{
    "firstName": "",
    "lastName": "",
    "email": "hola",
    "password": "12"
}

Sin validación, nuestra aplicación podría intentar procesar esos datos.

Eso es peligroso porque podemos terminar almacenando información incorrecta en la base de datos.

Con Validation podemos establecer reglas como:

firstName → obligatorio
lastName  → obligatorio
email     → debe tener formato de email
password  → mínimo 8 caracteres
3. ¿Cómo funciona?

Las reglas se definen mediante anotaciones.

Por ejemplo:

@NotBlank
private String firstName;

Estamos diciendo:

firstName no puede estar vacío.

Otro ejemplo:

@Email
private String email;

Estamos diciendo:

email debe tener un formato de correo electrónico válido.

4. @NotNull

Comprueba que un valor no sea null.

@NotNull
private String firstName;

Esto evita:

firstName = null

Pero hay algo importante:

@NotNull no evita un String vacío.

Por ejemplo:

""

no es null.

Por eso para textos normalmente utilizaremos @NotBlank.

5. @NotEmpty

Comprueba que el valor no sea null y que no esté vacío.

Por ejemplo:

@NotEmpty
private String firstName;

No permite:

null
""

Pero sí podría permitir:

"   "

porque contiene caracteres.

6. @NotBlank

Es especialmente útil para Strings.

@NotBlank
private String firstName;

No permite:

null
""
"   "

Por eso para nombres, apellidos, contraseñas y otros textos obligatorios suele ser más apropiado que @NotNull.

7. Diferencia entre @NotNull, @NotEmpty y @NotBlank
Anotación	null	""	" "
@NotNull	❌	✅	✅
@NotEmpty	❌	❌	✅
@NotBlank	❌	❌	❌

Esta diferencia es una muy buena pregunta de entrevista.

8. @Email

Comprueba que un String tenga un formato compatible con una dirección de email.

@Email
private String email;

Por ejemplo:

usuario@gmail.com

es válido.

Mientras que:

usuario

no cumple el formato esperado.

Importante

@Email no significa que el correo exista.

No comprueba:

"¿Existe realmente ese Gmail?"

Solamente valida el formato.

9. @Size

Permite establecer una longitud mínima y/o máxima.

Por ejemplo:

@Size(min = 8, max = 100)
private String password;

Esto indica:

mínimo → 8 caracteres
máximo → 100 caracteres
10. @Min y @Max

Se utilizan principalmente para valores numéricos.

Por ejemplo:

@Min(18)
private Integer age;

El valor debe ser como mínimo 18.

También:

@Max(100)
private Integer score;

El valor máximo sería 100.

11. @Positive

Indica que un número debe ser positivo.

@Positive
private BigDecimal salary;

No permite valores:

0
-100
12. @Past y @Future

También podemos validar fechas.

Por ejemplo:

@Past
private LocalDate birthDate;

La fecha debe estar en el pasado.

Mientras:

@Future
private LocalDate interviewDate;

requiere una fecha futura.

13. Validation y Spring Boot

Spring Boot integra Bean Validation con el manejo de las peticiones HTTP.

Por ejemplo, un DTO podría ser:

public class UserRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;
}

Y nuestro controller podría recibirlo utilizando:

@Valid

Por ejemplo:

@PostMapping
public ResponseEntity<?> create(@Valid @RequestBody UserRequest request) {
    ...
}

@Valid le indica a Spring:

"Ejecutá las validaciones definidas en este objeto antes de continuar."

14. ¿Qué pasa si falla una validación?

Supongamos que enviamos:

{
    "firstName": "",
    "lastName": "Perez",
    "email": "hola"
}

Spring detectará los errores antes de ejecutar la lógica del servicio.

Conceptualmente:

HTTP Request
     ↓
Controller
     ↓
@Valid
     ↓
Validation
     ↓
❌ Datos inválidos
     ↓
Error HTTP 400

El código HTTP esperado normalmente será:

400 Bad Request

Esto significa:

El cliente envió una petición que no cumple las reglas esperadas.

15. Validation NO es lo mismo que una restricción de base de datos.

Supongamos:

@NotBlank
private String email;

Esto valida los datos que llegan a nuestra aplicación.
Pero supongamos que queremos impedir que existan dos usuarios con:

email = juan@gmail.com

Eso es otra cosa.
Ahí necesitamos una restricción de unicidad en la base de datos.

Conceptualmente:

              Cliente
                 ↓
              @Valid
                 ↓
          Bean Validation
                 ↓
             Service
                 ↓
               JPA
                 ↓
             MySQL
                 ↓
          UNIQUE(email)

Son diferentes capas de protección.

16. ¿Dónde colocaremos las validaciones?

Acá aparece una decisión de arquitectura importante.

Podríamos poner:

@NotBlank
private String firstName;

directamente en User.

Pero más adelante vamos a utilizar DTOs.

Por ejemplo:

User
        → entidad de base de datos

UserRequest
        → datos que llegan para crear un usuario

UserResponse
        → datos que devolvemos al cliente

Esto nos permite separar:

API
 ↓
DTO
 ↓
Service
 ↓
Entity
 ↓
Database

Y evita mezclar las reglas de entrada de la API con el modelo de persistencia.

Por eso todavía no vamos a agregar las validaciones a User.

Primero vamos a crear los DTOs cuando lleguemos a la API.

17. Dependencia

En Spring Boot normalmente necesitamos la dependencia de Validation:

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>


18. Buenas prácticas
Validar en el límite de la aplicación

Los datos provenientes del cliente deben validarse antes de procesarlos.

No confiar solamente en Validation

La base de datos también debe tener restricciones importantes.

No utilizar @NotBlank para todo

Cada anotación tiene un propósito.

Separar DTOs de entidades

Las reglas de entrada de la API no necesariamente son iguales a las reglas del modelo de persistencia.

No guardar contraseñas sin protección

Validation puede comprobar que una contraseña tenga determinada longitud, pero no protege la contraseña.

La protección de contraseñas será responsabilidad de Spring Security y hashing.

19. Aplicación en Interview Coach AI

Para nuestro proyecto probablemente tendremos reglas como:

User
firstName → obligatorio
lastName → obligatorio
email → obligatorio + formato email
password → obligatorio + longitud mínima
Interview
company → obligatorio
position → obligatorio
language → obligatorio
Document
name → obligatorio
type → obligatorio

Estas reglas las iremos agregando cuando construyamos los DTO correspondientes.

20. Preguntas de entrevista

¿Qué es Jakarta Bean Validation?

Es una especificación para definir y ejecutar reglas de validación sobre objetos Java mediante anotaciones.

¿Qué diferencia hay entre @NotNull y @NotBlank?

@NotNull solamente impide null. 
@NotBlank, además, impide Strings vacíos o compuestos solamente por espacios.

¿Qué hace @Email?

Valida que un String tenga un formato compatible con una dirección de email.

¿Qué hace @Valid?

Le indica a Spring que ejecute las validaciones definidas sobre el objeto recibido.

¿Qué ocurre cuando falla una validación de un request?

Normalmente Spring responde con 400 Bad Request.

¿Validation reemplaza las restricciones de la base de datos?

No. Son mecanismos diferentes y complementarios.

¿Por qué usar DTOs?

Permiten separar el contrato de la API del modelo de persistencia y controlar qué datos entran y salen de nuestra aplicación.