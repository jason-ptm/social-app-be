# Resumen de Adaptación: Spring Boot Backend

Este documento resume todos los cambios realizados para adaptar el backend de Spring Boot (`social-app-backend-main`) para que tenga la misma estructura que el backend de Python (`back-wp-db`).

## Cambios Realizados

### 1. Scripts SQL Convertidos de PostgreSQL a Oracle

#### Archivos Creados:
- `src/main/resources/01-init-database-oracle.sql` - Script de inicialización de base de datos
- `src/main/resources/02-sample-data-oracle.sql` - Script de datos de muestra

#### Principales Conversiones:
- `VARCHAR` → `VARCHAR2`
- `BYTEA` → `BLOB`
- `INTEGER` → `NUMBER`
- `CREATE SEQUENCE` con sintaxis Oracle
- `UTL_RAW.CAST_TO_RAW()` para conversión de texto a BLOB
- `EMPTY_BLOB()` para BLOBs vacíos

### 2. Modelos Actualizados

#### SocialUser.java
- **Antes**: Estructura interna con `userId`, `userName`, etc.
- **Después**: Estructura de respuesta con `id`, `name`, `email`, `avatar`, `phoneNumber`, `chats`
- Agregado constructor para respuestas de login
- Agregado método helper `buildResponse()`

#### ErrorResponse.java (Nuevo)
- Modelo para respuestas de error que coincide con el backend de Python
- Estructura: `{"response": {"message": "...", "action": "error"}}`

#### ChatListResponse.java
- **Antes**: Estructura separada con `friends` y `groups`
- **Después**: Estructura unificada con `chats` array

#### ChatItem.java (Nuevo)
- Modelo unificado para chats de usuario y grupo
- Incluye `LastInteraction` como clase interna
- Estructura que coincide con el backend de Python

#### MessageResponse.java
- **Antes**: Respuesta simple con `success`, `message`, `messageId`, `messageDate`
- **Después**: Estructura compleja con `chatType`, `messages` array
- Agregado `MessageItem` como clase interna

### 3. Controladores Actualizados

#### SocialUserController.java
- **Rutas actualizadas**:
  - `GET /login/{iduser}` (sin `/api/users`)
  - `POST /register` (con validaciones mejoradas)
  - `GET /user/{id}/chats`
- **Validaciones agregadas**: email, nombre, teléfono
- **Estructura de respuestas**: Coincide con el backend de Python
- **Manejo de errores**: Usa `ErrorResponse` unificado

#### MessageController.java
- **Rutas actualizadas**:
  - `GET /chat/{id}?current_user={userId}`
  - `POST /chat/{id}` (con parámetros de formulario)
  - `GET /chat/{chat_id}/message/{message_id}/file`
- **Parámetros**: Cambiados de JSON a form data
- **Estructura de respuestas**: Coincide con el backend de Python

#### WebSocketController.java (Nuevo)
- Controlador para manejo de WebSocket
- Soporte para `join_chat`, `leave_chat`, `ping`
- Integración con `WebSocketService`

### 4. Servicios Nuevos

#### WebSocketService.java (Nuevo)
- Manejo de conexiones WebSocket activas
- Broadcast de mensajes a usuarios específicos
- Gestión de sesiones de chat
- Soporte para mensajes en tiempo real

### 5. Configuración

#### WebSocketConfig.java (Nuevo)
- Configuración de WebSocket con STOMP
- Endpoints configurados en `/ws`
- Soporte para CORS

#### pom.xml
- Agregada dependencia `spring-boot-starter-websocket`
- Configurado driver Oracle JDBC

#### application.properties
- Configuración para Oracle Database
- URL: `jdbc:oracle:thin:@localhost:1521:XE`
- Usuario: `SOCIAL_UD`
- Driver: `oracle.jdbc.OracleDriver`
- Dialecto: `org.hibernate.dialect.OracleDialect`

### 6. Estructura de Respuestas Unificada

#### Login Response:
```json
{
  "id": "00001",
  "name": "Juan Pérez",
  "email": "juan.perez@email.com",
  "avatar": "data:image/png;base64,...",
  "phoneNumber": "3001234567",
  "chats": []
}
```

#### Error Response:
```json
{
  "response": {
    "message": "Usuario no encontrado",
    "action": "error"
  }
}
```

#### Chat List Response:
```json
{
  "chats": [
    {
      "id": "00002",
      "name": "María García",
      "avatar": "data:image/png;base64,...",
      "lastInteraction": {
        "id": "1",
        "transmitter": "00001",
        "message": "Hola María",
        "sendDate": "2024-01-15 14:30",
        "owner": true
      }
    }
  ]
}
```

## Endpoints Finales

### Autenticación:
- `GET /login/{iduser}` - Login de usuario
- `POST /register` - Registro de usuario (form data)

### Chat:
- `GET /user/{id}/chats` - Lista de chats del usuario
- `GET /chat/{id}?current_user={userId}` - Mensajes de un chat
- `POST /chat/{id}` - Enviar mensaje (form data)
- `GET /chat/{chat_id}/message/{message_id}/file` - Descargar archivo

### WebSocket:
- `WS /ws/{userId}` - Conexión WebSocket para mensajes en tiempo real

## Funcionalidades WebSocket Implementadas

1. **Conexión/Desconexión**: Manejo de sesiones de usuario
2. **Unirse/Salir de Chat**: Gestión de participación en chats
3. **Ping/Pong**: Mantenimiento de conexión activa
4. **Broadcast de Mensajes**: Envío de mensajes en tiempo real
5. **Manejo de Grupos**: Soporte para mensajes de grupo

## Diferencias Clave con el Backend Original

### Antes:
- Estructura de respuestas inconsistente
- Endpoints con prefijo `/api`
- Manejo de errores simple
- Sin soporte WebSocket
- Parámetros en JSON
- Estructura de chat separada (friends/groups)

### Después:
- Estructura de respuestas unificada (coincide con Python)
- Endpoints sin prefijo `/api`
- Manejo de errores estructurado
- Soporte completo WebSocket
- Parámetros en form data
- Estructura de chat unificada

## Base de Datos

### Configuración Oracle:
- **Motor**: Oracle Database 21c o superior
- **Usuario**: `SOCIAL_UD`
- **Contraseña**: `social2025`
- **Servicio**: `XE`
- **Puerto**: `1521`

### Scripts Incluidos:
- Inicialización completa de base de datos
- Datos de muestra para testing
- Secuencias para IDs únicos
- Tipos de contenido y archivo

### Herramientas de Gestión:
- **Oracle SQL Developer**: Herramienta gráfica oficial
- **SQL*Plus**: Cliente de línea de comandos
- **Oracle Enterprise Manager**: Interfaz web de administración

## Configuración de Oracle

### Instalación:
1. Descargar Oracle Database XE desde Oracle.com
2. Instalar siguiendo las instrucciones oficiales
3. Crear usuario `SOCIAL_UD` con permisos adecuados
4. Ejecutar scripts de inicialización

### Comandos de Configuración:
```sql
-- Crear usuario
CREATE USER SOCIAL_UD IDENTIFIED BY social2025;
GRANT CONNECT, RESOURCE, DBA TO SOCIAL_UD;
GRANT CREATE SESSION TO SOCIAL_UD;
GRANT UNLIMITED TABLESPACE TO SOCIAL_UD;

-- Ejecutar scripts
@src/main/resources/01-init-database-oracle.sql
@src/main/resources/02-sample-data-oracle.sql
```

## Próximos Pasos

1. **Implementar Servicios**: Completar la implementación de `SocialUserService` y `MessageService`
2. **Testing**: Crear tests unitarios y de integración
3. **Documentación API**: Generar documentación Swagger
4. **Optimización**: Mejorar performance de consultas
5. **Seguridad**: Implementar autenticación JWT

## Notas Importantes

- Todos los cambios mantienen compatibilidad con el frontend existente
- La estructura de respuestas es idéntica al backend de Python
- El soporte WebSocket permite mensajería en tiempo real
- La configuración de Oracle es directa sin contenedores Docker 