# Arquitectura

El proyecto seguirá una arquitectura en capas.

controller

Recibe las peticiones HTTP.

↓

service

Contiene la lógica del negocio.

↓

repository

Accede a la base de datos.

↓

entity

Representa las tablas.

---

Packages

config

Configuraciones.

controller

Endpoints REST.

service

Lógica de negocio.

repository

Persistencia.

entity

Modelo del dominio.

dto

Objetos de entrada y salida.

exception

Manejo de errores.

util

Clases auxiliares.