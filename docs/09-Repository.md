1. ¿Qué es un Repository?
Un Repository es un patrón de diseño que encapsula la lógica de almacenamiento, recuperación y búsqueda de datos. Actúa como una colección de objetos en memoria, aislando la capa de negocio de los detalles de la base de datos.

2. ¿Qué es Spring Data JPA?
Es un módulo de Spring que reduce drásticamente el código repetitivo (boilerplate) necesario para implementar la capa de acceso a datos. Permite trabajar con JPA mediante interfaces, encargándose de generar automáticamente las implementaciones en tiempo de ejecución.

3. ¿Qué es JpaRepository?
Es una interfaz específica de Spring Data JPA que extiende otras interfaces como CrudRepository y PagingAndSortingRepository. Añade métodos específicos de JPA, como la capacidad de vaciar el contexto de persistencia (flush) o eliminar registros en lotes (batch).

4. Relación Repository → JPA → Hibernate → MySQL
La interacción se organiza en capas que van desde la abstracción más alta hasta el almacenamiento físico:Repository (Spring Data JPA): Es la interfaz de alto nivel que usas en tu código para interactuar con los datos.JPA (Jakarta Persistence): Es la especificación oficial de Java (el estándar/contrato) que define cómo mapear objetos a tablas.Hibernate: Es el proveedor o la implementación real de JPA. Se encarga de traducir las operaciones de objetos a consultas SQL.MySQL: Es el sistema de gestión de bases de datos relacionales donde se guardan físicamente las tablas y los registros.

5. Métodos heredados de JpaRepository
Al heredar de JpaRepository, obtienes automáticamente métodos listos para usar, tales como:save(entity): Guarda o actualiza una entidad.findById(id): Busca un registro por su clave primaria.findAll(): Recupera todos los registros de la tabla.deleteById(id): Elimina un registro usando su clave primaria.saveAndFlush(entity): Guarda la entidad y fuerza la escritura inmediata en la base de datos.

6. ¿Qué significan los genéricos User y Long?
Cuando defines interface UserRepository extends JpaRepository<User, Long>, indicas lo siguiente:User (Primer parámetro): Especifica la clase de la entidad que el repositorio va a gestionar (la tabla User).Long (Segundo parámetro): Especifica el tipo de dato que tiene la clave primaria (@Id) definida dentro de esa entidad.

7. Responsabilidad del Repository
Su única misión es gestionar el acceso a los datos. Esto incluye realizar operaciones CRUD, ejecutar consultas personalizadas (mediante métodos de consulta o anotaciones @Query) y comunicar la base de datos con la aplicación.

8. Qué NO debería hacer el Repository
No debe contener lógica de negocio: No debe validar reglas de la empresa ni realizar cálculos complejos.No debe transformar datos de presentación: No debe mapear entidades a DTOs.No debe controlar flujos de la aplicación: No debe decidir qué responder al cliente o usuario final.

9. Flujo Controller → Service → Repository → MySQL
El camino que siguen los datos en una arquitectura limpia es el siguiente:Controller: Recibe la solicitud HTTP del cliente, extrae los datos y los envía al servicio.Service: Contiene la lógica de negocio, coordina las transacciones y procesa las reglas de la aplicación. Llama al repositorio.Repository: Recibe la orden del servicio y utiliza Spring Data / Hibernate para traducir la petición en una consulta SQL.MySQL: Ejecuta la consulta SQL, persiste o recupera la información y devuelve el resultado físico hacia las capas superiores.