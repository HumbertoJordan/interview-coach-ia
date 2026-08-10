# DTOs — Data Transfer Objects

## 1. ¿Qué es un DTO?

DTO significa **Data Transfer Object**.

Es un objeto utilizado para transportar datos entre diferentes partes de una aplicación.

En nuestra aplicación utilizamos DTOs para definir qué información:

- recibimos desde el cliente;
- devolvemos al cliente;
- utilizamos como respuesta de éxito;
- utilizamos como respuesta de error.

Los DTOs ayudan a separar el modelo interno de nuestra aplicación del contrato que exponemos mediante nuestra API REST.

---

# 2. ¿Por qué utilizamos DTOs?

Nuestra entidad `User` representa principalmente la información que necesitamos persistir en la base de datos.

Por ejemplo:

```text
User
├── id
├── firstName
├── lastName
├── email
├── password
├── enabled
├── createdAt
└── updatedAt