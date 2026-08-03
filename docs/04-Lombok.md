¿Qué es Lombok?

Lombok es una biblioteca para Java que permite reducir código repetitivo (boilerplate) mediante anotaciones.

En una clase Java normalmente tenemos que escribir manualmente métodos como:
getters
setters

qué problema resuelve;
En una aplicación Spring Boot podemos tener muchas entidades y DTOs.
Por ejemplo, una entidad con 10 atributos podría necesitar decenas de métodos:

Lombok utiliza anotaciones que son procesadas durante la compilación.

Por ejemplo:

@Getter

le indica a Lombok que genere los métodos get...() correspondientes.

@Setter

genera los métodos set...().Principales anotaciones
@Getter

Genera getters para los atributos.

@Getter
public class User {

    private String firstName;
}

Lombok genera conceptualmente:

public String getFirstName() {
    return firstName;
}
@Setter

Genera setters.

@Setter
public class User {

    private String firstName;
}

Conceptualmente genera:

public void setFirstName(String firstName) {
    this.firstName = firstName;
}
@NoArgsConstructor

Genera un constructor sin argumentos.

@NoArgsConstructor
public class User {
}

Conceptualmente:

public User() {
}

Esto es especialmente importante en entidades JPA porque Hibernate necesita poder crear instancias de las entidades.

@AllArgsConstructor

Genera un constructor con todos los atributos.

Por ejemplo:

@AllArgsConstructor
public class User {

    private Long id;
    private String firstName;
}

Conceptualmente:

public User(Long id, String firstName) {
    this.id = id;
    this.firstName = firstName;
}
@RequiredArgsConstructor

Genera un constructor utilizando los campos final y los campos marcados como @NonNull.

Es muy utilizado en Spring para inyección por constructor.

Por ejemplo:

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
}

Spring puede utilizar automáticamente ese constructor para inyectar UserRepository.

@ToString

Genera un método toString().

Por ejemplo:

@ToString
public class User {
    
    private String firstName;
    private String lastName;
}

Permite obtener una representación textual del objeto.

Pero hay que tener cuidado con las entidades JPA y las relaciones.

Si una entidad tiene relaciones entre objetos, un toString() mal diseñado puede provocar problemas como referencias circulares o consultas innecesarias a la base de datos.

@EqualsAndHashCode

Genera:

equals()
hashCode()

Estos métodos se utilizan para comparar objetos y trabajar correctamente con estructuras como:

Set
HashSet
HashMap

En entidades JPA hay que utilizar esta anotación con cuidado porque la identidad de una entidad puede estar relacionada con su ID y con el ciclo de vida de Hibernate.

@Data

Existe una anotación que agrupa varias funcionalidades:

@Data

Incluye, entre otras cosas:

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor

Parece muy cómodo, pero no significa que debamos poner @Data en todas nuestras clases.

En entidades JPA prefiero utilizar anotaciones específicas:

@Getter
@Setter
@NoArgsConstructor

porque tenemos más control sobre qué genera Lombok.

Ventajas
Menos código

Reduce considerablemente el boilerplate.

Mayor legibilidad

La clase muestra principalmente sus atributos y comportamiento.

Menos código repetitivo

No tenemos que escribir manualmente decenas de getters y setters.

Integración con Spring

Funciona muy bien con Spring Boot, especialmente para constructores e inyección de dependencias.

Desventajas

Lombok también tiene algunos aspectos que debemos conocer.

El código generado no está escrito explícitamente

Un desarrollador que no conozca Lombok puede preguntarse:

"¿De dónde salió este getter?"

Por eso es importante conocer las anotaciones.

Dependencia del IDE

El IDE debe tener soporte para Lombok para mostrar correctamente los métodos generados y evitar falsos errores.

Puede ocultar demasiado código

Usar:

@Data

indiscriminadamente puede generar métodos que realmente no necesitamos.

Hay que tener cuidado con JPA

Especialmente con:

@ToString
@EqualsAndHashCode
@Data

cuando existen relaciones entre entidades.

¿Por qué utilizaremos Lombok en Interview Coach AI?

Lo utilizaremos para reducir código repetitivo en nuestras entidades, DTOs y determinadas clases de servicio.

Para nuestras entidades JPA comenzaremos utilizando de manera explícita:

@Getter
@Setter
@NoArgsConstructor

En lugar de utilizar directamente:

@Data

Esto nos permite controlar mejor qué genera Lombok.

Ejemplo aplicado a User

Nuestra clase todavía está incompleta.

La idea será llegar a algo conceptualmente similar a:

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

Pero todavía no la copies ni la modifiques.

Nos falta terminar de estudiar JPA y colocar correctamente @Table, @Id, @GeneratedValue y los demás elementos.

Preguntas de entrevista

Estas son buenas preguntas para agregar al final de tu documentación:

¿Qué es Lombok?

Una biblioteca de Java que permite reducir código repetitivo mediante anotaciones que generan código durante la compilación.

¿Qué hace @Getter?

Genera automáticamente los métodos getter de los atributos.

¿Qué hace @Setter?

Genera automáticamente los métodos setter.

¿Qué hace @NoArgsConstructor?

Genera un constructor sin argumentos.

¿Qué diferencia hay entre @Data y @Getter + @Setter?

@Data genera varias funcionalidades adicionales, incluyendo getters, setters, toString, equals, hashCode y un constructor requerido. Usar @Getter y @Setter explícitamente permite tener mayor control.

¿Usarías @Data en una entidad JPA?

No como regla general. Es preferible utilizar anotaciones específicas porque equals, hashCode, toString y los setters pueden tener implicaciones en entidades administradas por Hibernate.

 