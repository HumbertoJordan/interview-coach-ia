(ApiResponseSuccessDto)
El problema que resuelve

Sin un formato estándar, cada endpoint de tu API podría devolver JSON con forma distinta:

json
// endpoint A
{ "id": 1, "firstName": "Tony" }

// endpoint B
{ "user": { "id": 1 }, "ok": true }

El cliente (frontend, Postman, otro servicio) nunca sabe qué esperar. ApiResponseSuccessDto<T> es el "sobre" único que envuelve toda respuesta exitosa de tu API, sea cual sea el recurso.

ApiResponseSuccessDto<T>
java
@Getter
@Setter
@NoArgsConstructor
public class ApiResponseSuccessDto<T> {
    private Boolean success;
    private String message;
    private T data;
}

Siempre trae la misma forma, sin importar el endpoint:

json
{
  "success": true,
  "message": "texto descriptivo de lo que pasó",
  "data": { /* acá va lo específico del recurso */ }
}
Genéricos <T>

El <T> es un tipo genérico — un "comodín" que se define recién cuando usás la clase, no cuando la escribís.

Sin genéricos, tendrías que crear una clase de respuesta por cada recurso:

java
// SIN genéricos → repetición
public class UserApiResponse {
    private Boolean success;
    private String message;
    private UserResponseDto data;
}

public class InterviewApiResponse {
    private Boolean success;
    private String message;
    private InterviewResponseDto data;
}
// ... una clase más por cada entidad futura (CV, Report, etc.)

Con genéricos, una sola clase sirve para todo:

java
// CON genéricos → una sola clase, reutilizable
ApiResponseSuccessDto<UserResponseDto>
ApiResponseSuccessDto<InterviewResponseDto>
ApiResponseSuccessDto<List<UserResponseDto>>

El compilador reemplaza T por el tipo real que le pasaste en cada uso. Es el mismo principio que ya usás en JpaRepository<User, Long> — User y Long son los genéricos ahí.

data

Es donde va el contenido específico de la operación — en tu caso, un UserResponseDto. Es de tipo T, así que puede ser cualquier cosa: un objeto, una lista, o incluso null si no aplica (por ejemplo, en un DELETE exitoso).

success

Un booleano simple y explícito. Aunque el status HTTP (201, 400, etc.) ya indica si algo salió bien o mal, tener success en el body le da al cliente una forma rápida de chequear sin tener que interpretar códigos HTTP:

javascript
if (response.data.success) { ... }

Es redundante con el status HTTP a propósito — no todos los consumidores de tu API leen el status code con la misma atención, pero todos leen el body.

message

Texto pensado para humanos (o para mostrarse directo en un frontend), no para lógica de negocio. Ejemplos:

text
"Usuario creado correctamente"
"El email ya está registrado"
"Entrevista finalizada con éxito"

Nunca deberías tomar decisiones de código en base al contenido de message (eso es lo que es success y el status HTTP para). message es solo información.

Diferencia entre UserResponseDto y ApiResponseSuccessDto

Son dos capas distintas que cumplen roles distintos y no se pisan:

	UserResponseDto	ApiResponseSuccessDto<T>
Qué representa	El recurso en sí (un usuario)	El sobre estándar de toda respuesta de la API
Es específico de	La entidad User	Nada — es genérico, sirve para cualquier entidad
Contiene	id, firstName, email, etc.	success, message, data
Cuántas clases existen	Una por cada entidad (UserResponseDto, InterviewResponseDto...)	Una sola, reutilizada con genéricos
Se anida dentro de	—	Va dentro de data

En resumen:

text
UserResponseDto        → QUÉ dato viaja
ApiResponseSuccessDto  → CÓMO viaja ese dato (envoltorio consistente)

Un UserResponseDto jamás viaja "solo" en tus respuestas — siempre va envuelto dentro de ApiResponseSuccessDto.data. Esa es la regla que mantiene consistente toda tu API a medida que sumes más entidades (Interview, CV, Report...).