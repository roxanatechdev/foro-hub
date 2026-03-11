# Foro Hub - API Rest 💬

## 💻 Descripcion
**Foro Hub** es una API REST que permite la gestión eficiente de usuarios y la interacción en foros de discusión a traves de tópicos. Con funcionalidades para crear y administrar tópicos y respuestas mediante autenticación, diseñada como un proyecto backend, incorpora buenas prácticas de desarrollo y herramientas modernas para ofrecer una solución robusta y escalable.

## 🛠️ Tecnologías Utilizadas 
- **Java** (JDK 17)
- **Spring Boot 3**
- **PostgreSQL** (Base de datos relacional)
- **Spring Security** (Autenticación y autorización)
- **Flyway** (Migraciones de base de datos)
- **Swagger UI** (Documentación interactiva de la API)
- **Lombok** (Reducción de boilerplate en el código)
- **Auth0** (Proveedor de autenticación)

## 🚀 Instalación y Ejecución
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Anny-rox/Foro-hub
   cd foro-hub
   ```

2. Configurar las variables de entorno:
   - Configurar la conexión a PostgreSQL (usuario, contraseña y base de datos).
   - Incluir las credenciales de Auth0 para autenticación.

3. Ejecutar las migraciones de Flyway:
   ```bash
   ./mvnw flyway:migrate
   ```

4. Iniciar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Acceder a Swagger UI para probar los endpoints:
   - URL: `http://localhost:8080/swagger-ui.html`

## ⚙️ Características Principales
- Autenticación segura mediante Auth0.
- CRUD completo para la gestión de tópicos.
- Validaciones para evitar datos duplicados en títulos y mensajes.
- Documentación interactiva con Swagger.
- Uso de Flyway para gestión de migraciones de base de datos.

## 🗃️ Estructura del Proyecto
```
main
├── java
│   └── foro
│       └── hub
│           └── api
│               ├── controller
│               ├── domain
│               │   ├── topico
│               │   └── usuario
│               └── infra
│                   ├── errores
│                   ├── security
│                   └── springdoc
└── resources
    ├── db
    │   └── migration
    ├── static
    └── templates
```

## 📂 Paquetes principales:
- **controller**: Contiene los controladores para manejar las solicitudes de la API (e.g., `TopicoController`, `AutenticacionController`).
- **domain**: Define las entidades y DTOs del dominio del foro (e.g., `Topico`, `Usuario`).
- **infra**: Contiene configuraciones de seguridad, manejo de errores y documentación.

## 👉 Endpoints de la API
| Método | Endpoint           | Descripción                         |
|--------|--------------------|-------------------------------------|
| POST   | `/topicos`         | Crear un nuevo tópico.              |
| GET    | `/topicos`         | Listar todos los tópicos creados.   |
| GET    | `/topicos/{id}`    | Mostrar un tópico específico.      |
| PUT    | `/topicos/{id}`    | Actualizar un tópico existente.     |
| DELETE | `/topicos/{id}`    | Eliminar un tópico.                 |



## 📝 Licencia
Este proyecto está bajo la licencia MIT. 

