# Oracle Database Setup for Social App Backend

Este documento contiene las instrucciones para configurar la base de datos Oracle para el backend de Spring Boot.

## Requisitos Previos

1. Oracle Database 21c o superior instalado y funcionando
2. Usuario `SOCIAL_UD` creado con permisos adecuados
3. Conexión configurada en `application.properties`

## Instalación de Oracle Database

### Opción 1: Oracle Database Express Edition (XE)
1. Descargar Oracle Database XE desde [Oracle.com](https://www.oracle.com/database/technologies/xe-downloads.html)
2. Seguir las instrucciones de instalación oficiales
3. Durante la instalación, anotar:
   - Contraseña del usuario SYS
   - Puerto del listener (por defecto 1521)
   - Nombre del servicio (por defecto XE)

### Opción 2: Oracle Database Standard/Enterprise
1. Descargar desde [Oracle.com](https://www.oracle.com/database/technologies/)
2. Usar Database Configuration Assistant (DBCA) para crear la base de datos
3. Configurar el listener y servicios

## Configuración de la Base de Datos

### 1. Crear el Usuario y Esquema

```sql
-- Conectar como SYSDBA
sqlplus / as sysdba

-- Crear usuario
CREATE USER SOCIAL_UD IDENTIFIED BY social2025;

-- Otorgar permisos
GRANT CONNECT, RESOURCE, DBA TO SOCIAL_UD;
GRANT CREATE SESSION TO SOCIAL_UD;
GRANT UNLIMITED TABLESPACE TO SOCIAL_UD;
GRANT CREATE TABLE TO SOCIAL_UD;
GRANT CREATE SEQUENCE TO SOCIAL_UD;
GRANT CREATE VIEW TO SOCIAL_UD;
GRANT CREATE PROCEDURE TO SOCIAL_UD;

-- Salir
EXIT;
```

### 2. Ejecutar los Scripts SQL

Los scripts están ubicados en `src/main/resources/` y deben ejecutarse en el siguiente orden:

#### a) Inicialización de la Base de Datos
```bash
# Conectar como SOCIAL_UD
sqlplus SOCIAL_UD/social2025@//localhost:1521/XE

# Ejecutar script de inicialización
@src/main/resources/01-init-database-oracle.sql
```

#### b) Datos de Muestra
```bash
# Continuar en la misma sesión o conectar nuevamente
@src/main/resources/02-sample-data-oracle.sql
```

## Diferencias con PostgreSQL

### Cambios Principales en la Conversión:

1. **Tipos de Datos:**
   - `VARCHAR` → `VARCHAR2`
   - `BYTEA` → `BLOB`
   - `INTEGER` → `NUMBER`
   - `TIMESTAMP` → `TIMESTAMP`

2. **Secuencias:**
   - `CREATE SEQUENCE` con sintaxis Oracle
   - `NOCACHE` y `NOCYCLE` en lugar de `NO CYCLE`

3. **Funciones:**
   - `UTL_RAW.CAST_TO_RAW()` para convertir texto a BLOB
   - `EMPTY_BLOB()` para BLOBs vacíos

4. **Intervalos de Tiempo:**
   - `INTERVAL '1' HOUR` en lugar de `INTERVAL '1 hour'`

## Estructura de Tablas

### Tablas Principales:
- `SOCIAL_USER`: Usuarios del sistema
- `USER_FRIENDSHIP`: Relaciones de amistad
- `SOCIAL_GROUP`: Grupos de chat
- `GROUP_MEMBERSHIP`: Miembros de grupos
- `MESSAGE`: Mensajes
- `CONTENT`: Contenido de mensajes (texto, archivos, etc.)

### Secuencias:
- `USER_ID_SEQ`: Para IDs de usuario
- `GROUP_ID_SEQ`: Para IDs de grupo
- `MESSAGE_ID_SEQ`: Para IDs de mensaje

## Configuración de la Aplicación

### application.properties
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=SOCIAL_UD
spring.datasource.password=social2025
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

## Herramientas de Gestión

### Oracle SQL Developer
- Herramienta gráfica oficial de Oracle
- Descargar desde [Oracle.com](https://www.oracle.com/database/sqldeveloper/technologies/download/)
- Conectar usando:
  - Host: localhost
  - Port: 1521
  - Service: XE
  - Username: SOCIAL_UD
  - Password: social2025

### SQL*Plus
- Cliente de línea de comandos incluido con Oracle
- Comando de conexión: `sqlplus SOCIAL_UD/social2025@//localhost:1521/XE`

### Oracle Enterprise Manager
- Interfaz web de administración
- Accesible en: http://localhost:5500/em
- Usar credenciales de SYSDBA

## Endpoints Adaptados

### Autenticación:
- `GET /login/{iduser}` - Login de usuario
- `POST /register` - Registro de usuario

### Chat:
- `GET /user/{id}/chats` - Lista de chats del usuario
- `GET /chat/{id}?current_user={userId}` - Mensajes de un chat
- `POST /chat/{id}` - Enviar mensaje

### WebSocket:
- `WS /ws/{userId}` - Conexión WebSocket para mensajes en tiempo real

## Estructura de Respuestas

Las respuestas han sido adaptadas para coincidir con el backend de Python:

### Login Response:
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

### Error Response:
```json
{
  "response": {
    "message": "Usuario no encontrado",
    "action": "error"
  }
}
```

### Chat List Response:
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

## WebSocket

El sistema incluye soporte para WebSocket para mensajes en tiempo real:

### Tipos de Mensajes:
- `join_chat`: Unirse a un chat
- `leave_chat`: Salir de un chat
- `ping`: Mantener conexión activa
- `new_message`: Nuevo mensaje recibido

## Notas Importantes

1. **BLOBs**: Los archivos se almacenan como BLOBs en Oracle
2. **Secuencias**: Se usan secuencias Oracle para generar IDs únicos
3. **Timestamps**: Se usan timestamps Oracle para fechas
4. **Conexiones**: Asegúrate de que el pool de conexiones esté configurado correctamente

## Troubleshooting

### Error de Conexión:
- Verificar que Oracle esté ejecutándose: `lsnrctl status`
- Verificar credenciales en `application.properties`
- Verificar que el usuario `SOCIAL_UD` tenga permisos adecuados
- Verificar que el servicio XE esté activo

### Error de Scripts:
- Ejecutar scripts como usuario `SOCIAL_UD`
- Verificar que las secuencias se creen correctamente
- Verificar que las tablas se creen en el esquema correcto

### Verificar Estado de Oracle:
```bash
# Verificar listener
lsnrctl status

# Verificar servicios
lsnrctl services

# Conectar y verificar tablas
sqlplus SOCIAL_UD/social2025@//localhost:1521/XE
SELECT table_name FROM user_tables;
``` 