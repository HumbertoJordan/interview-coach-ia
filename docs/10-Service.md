# Service Layer — Capa de Servicio

## 1. ¿Qué es la capa Service?

La capa Service es la parte de nuestra aplicación donde colocamos la **lógica de negocio**.

Su responsabilidad principal es recibir una operación solicitada por el Controller, ejecutar las reglas necesarias y utilizar los componentes correspondientes para completar esa operación.

En nuestra arquitectura:

```text
Cliente
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Base de datos

2. ¿Por qué necesitamos un Service?

Podríamos colocar toda la lógica directamente dentro del Controller, pero esto produciría Controllers demasiado grandes y difíciles de mantener.

Por ejemplo, imaginemos que nuestro Controller tuviera que:

recibir el usuario;
validar reglas de negocio;
comprobar si el email ya existe;
crear la entidad User;
guardar el usuario;
convertir la entidad en UserResponseDto;
construir la respuesta HTTP.

El Controller terminaría teniendo demasiadas responsabilidades.

En lugar de eso, dividimos las responsabilidades:

Controller
    ↓
recibe y responde HTTP

Service
    ↓
ejecuta lógica de negocio

Repository
    ↓
accede a la base de datos

Esto permite que cada capa tenga una responsabilidad clara.

3. Responsabilidad del Controller

El Controller pertenece a la capa de presentación de nuestra API REST.

Su responsabilidad principal es trabajar con HTTP.

Por ejemplo:

POST /api/users
GET /api/users
GET /api/users/{id}
DELETE /api/users/{id}

El Controller recibe una solicitud y delega el trabajo al Service.

No debería contener la lógica principal de negocio.

Ejemplo conceptual:

HTTP Request
     ↓
UserController
     ↓
UserService
4. Responsabilidad del Service

El Service contiene la lógica de negocio de nuestra aplicación.

Por ejemplo, al crear un usuario podemos necesitar:

1. Recibir UserRequestDto
2. Comprobar si el email ya existe
3. Crear User
4. Establecer valores iniciales
5. Guardar User
6. Convertir User en UserResponseDto
7. Devolver el resultado

Estas operaciones pertenecen a la lógica de nuestra aplicación y no deberían estar directamente en el Controller.

5. Responsabilidad del Repository

El Repository pertenece a la capa de acceso a datos.

Su responsabilidad es comunicarse con la base de datos.

Nuestro:

UserRepository

extiende:

JpaRepository<User, Long>

y permite utilizar operaciones como:

save()
findById()
findAll()
deleteById()
existsById()

El Repository no debería encargarse de decidir las reglas de negocio.

6. Comparación entre las tres capas
Controller

Trabaja con:

HTTP
Request
Response
Status Codes
DTOs

Su responsabilidad es recibir y responder solicitudes.

Service

Trabaja con:

Lógica de negocio
Reglas de negocio
Entidades
DTOs
Repositories

Su responsabilidad es ejecutar las operaciones de la aplicación.

Repository

Trabaja con:

Persistencia
Base de datos
JPA
Hibernate

Su responsabilidad es acceder a los datos.

7. Arquitectura

Nuestra aplicación utiliza una separación por capas:

┌──────────────────────┐
│       Cliente        │
└──────────┬───────────┘
           │ HTTP
           ▼
┌──────────────────────┐
│     Controller       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│       Service        │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│      Repository      │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│       MySQL          │
└──────────────────────┘

Cada capa tiene una responsabilidad diferente.

8. ¿Qué es @Service?

Spring proporciona la anotación:

@Service

que se utiliza para indicar que una clase pertenece a la capa de servicio.

Ejemplo:

@Service
public class UserService {

}

Cuando Spring inicia la aplicación, detecta esta clase y la registra como un componente administrado por Spring.

Esto permite que otros componentes, como nuestro Controller, puedan utilizar UserService.

9. Inyección de dependencias

Nuestro UserService necesita utilizar UserRepository.

No queremos crear manualmente el Repository:

new UserRepository()

porque UserRepository es una interfaz administrada por Spring Data JPA.

Spring se encarga de proporcionar la implementación necesaria.

Por eso utilizamos inyección de dependencias.

La idea es:

UserService
     │
     │ necesita
     ▼
UserRepository

Spring proporciona automáticamente esa dependencia.

10. Inyección mediante constructor

Una forma recomendada de realizar la inyección es mediante el constructor.

Conceptualmente:

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

El constructor recibe el Repository y Spring proporciona la instancia correspondiente.

La dependencia se declara como:

private final UserRepository userRepository;

El final indica que la referencia debe ser inicializada y no será reemplazada posteriormente.

11. Lombok y @RequiredArgsConstructor

Como nuestro proyecto utiliza Lombok, podemos simplificar el constructor utilizando:

@RequiredArgsConstructor

Ejemplo:

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}

Lombok genera automáticamente un constructor para los atributos final.

Conceptualmente, Lombok genera:

public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}

Por lo tanto:

@RequiredArgsConstructor

reduce código repetitivo sin eliminar la inyección mediante constructor.

12. Ejemplo de flujo al crear un usuario

Supongamos que el cliente envía:

POST /api/users

con:

{
    "firstName": "Juan",
    "lastName": "Perez",
    "email": "juan@gmail.com",
    "password": "12345678"
}

El flujo será:

Cliente
   ↓
POST /api/users
   ↓
UserController
   ↓
UserRequestDto
   ↓
@Valid
   ↓
UserService
   ↓
UserRepository
   ↓
MySQL
13. El Service y UserRepository

Nuestro Service tendrá una dependencia:

private final UserRepository userRepository;

Esto permite realizar operaciones como:

userRepository.save(user);

El Service decide cuándo necesita guardar un usuario.

El Repository sabe cómo acceder a la base de datos.

Esta diferencia es importante.

Service
"Necesito guardar este usuario"

       ↓

Repository
"Yo me encargo de persistirlo"
14. Reglas de negocio

Una regla de negocio es una condición o comportamiento que forma parte de la lógica de nuestra aplicación.

Por ejemplo:

Un email no puede estar registrado dos veces.

El Controller no debería decidir esto.

El Repository tampoco debería decidirlo.

El Service puede realizar la comprobación:

UserService
     ↓
¿Existe el email?
     │
     ├── Sí → lanzar excepción
     │
     └── No → continuar

De esta forma las reglas de negocio quedan centralizadas en la capa Service.

15. Conversión entre DTO y Entity

El Service también puede encargarse de convertir los datos recibidos mediante DTO en una entidad.

Por ejemplo:

UserRequestDto
      ↓
      ↓ conversión
      ↓
User

El DTO contiene:

firstName
lastName
email
password

y el Service puede utilizar esos datos para construir un objeto User.

Después:

User
 ↓
Repository
 ↓
Database
16. Conversión de Entity a Response DTO

Después de guardar el usuario, normalmente no queremos devolver directamente la entidad.

El Service puede convertir:

User
 ↓
UserResponseDto

De esta forma:

password

queda fuera de la respuesta.

El flujo completo sería:

UserRequestDto
      ↓
    User
      ↓
UserRepository
      ↓
    MySQL
      ↓
    User
      ↓
UserResponseDto
17. Service y respuestas HTTP

El Service no debería encargarse directamente de los códigos HTTP.

Por ejemplo:

201 Created
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error

Estos pertenecen principalmente a la capa HTTP.

El Controller puede utilizar el resultado del Service para construir:

ResponseEntity

Por lo tanto:

Service
   ↓
resultado / excepción
   ↓
Controller
   ↓
ResponseEntity
   ↓
HTTP

Esto mantiene separadas la lógica de negocio y la lógica HTTP.

18. Flujo de errores

Si el Service detecta una situación incorrecta, puede lanzar una excepción específica.

Por ejemplo:

Email ya registrado

El Service puede lanzar:

UserAlreadyExistsException

Luego nuestro:

GlobalExceptionHandler

puede capturar esa excepción y construir:

ApiResponseErrorDto

El flujo será:

UserService
    ↓
Exception
    ↓
GlobalExceptionHandler
    ↓
ApiResponseErrorDto
    ↓
ResponseEntity
    ↓
HTTP Response
19. ¿Qué NO debería hacer el Service?

El Service no debería convertirse en una clase que haga absolutamente todo.

Por ejemplo, no debería encargarse directamente de:

HTTP headers
HTTP status
RequestMapping
PostMapping
GetMapping

Eso corresponde al Controller.

Tampoco debería implementar directamente consultas SQL.

Eso corresponde al Repository / capa de persistencia.

20. Separación de responsabilidades

La idea principal de nuestra arquitectura es:

Controller
    ↓
HTTP

Service
    ↓
Lógica de negocio

Repository
    ↓
Persistencia

Cada capa tiene una responsabilidad específica.

Esto permite:

facilitar el mantenimiento;
reducir el acoplamiento;
facilitar las pruebas;
reutilizar lógica;
mantener Controllers pequeños;
separar la lógica de negocio de HTTP;
separar la lógica de negocio del acceso a datos.
21. Arquitectura actual de Interview Coach

Nuestro proyecto empieza a tomar esta forma:

com.interviewcoach
│
├── controller
│   └── UserController
│
├── dto
│   ├── UserRequestDto
│   ├── UserResponseDto
│   ├── ApiResponseSuccessDto
│   └── ApiResponseErrorDto
│
├── entity
│   └── User
│
├── repository
│   └── UserRepository
│
└── service
    └── UserService

El flujo principal será:

                    CLIENTE
                       │
                       │ HTTP
                       ▼
              ┌─────────────────┐
              │ UserController  │
              └────────┬────────┘
                       │
                 UserRequestDto
                       │
                     @Valid
                       │
                       ▼
              ┌─────────────────┐
              │   UserService   │
              └────────┬────────┘
                       │
                 lógica de negocio
                       │
                       ▼
              ┌─────────────────┐
              │ UserRepository  │
              └────────┬────────┘
                       │
                       ▼
                     MySQL

Para la respuesta:

MySQL
  ↓
User
  ↓
UserResponseDto
  ↓
ApiResponseSuccessDto<T>
  ↓
ResponseEntity
  ↓
Cliente

Para errores:

Exception
    ↓
GlobalExceptionHandler
    ↓
ApiResponseErrorDto
    ↓
ResponseEntity
    ↓
Cliente
22. Conceptos que debemos recordar
Controller

Gestiona las solicitudes y respuestas HTTP.

Service

Contiene la lógica de negocio.

Repository

Gestiona el acceso a los datos.

@Service

Indica a Spring que una clase pertenece a la capa Service.

Inyección de dependencias

Permite que Spring proporcione automáticamente las dependencias que necesita una clase.

Constructor Injection

Forma de inyectar dependencias mediante el constructor.

@RequiredArgsConstructor

Anotación de Lombok que genera automáticamente un constructor para los atributos final.

Separación de responsabilidades

Cada capa debe encargarse de una responsabilidad específica.

23. Regla mental para recordar la arquitectura

Cuando estés construyendo una aplicación Spring Boot, pensá:

¿Tiene que ver con HTTP?
        ↓
    Controller

¿Tiene que ver con reglas de negocio?
        ↓
      Service

¿Tiene que ver con guardar o consultar datos?
        ↓
    Repository

Esta regla nos ayudará a decidir dónde colocar cada parte del código.


### Lo que quiero que te quede claro

En este punto ya tenemos una arquitectura bastante definida:

```text
DTO → qué entra y qué sale
Controller → HTTP
Service → lógica de negocio
Repository → datos
Entity → persistencia