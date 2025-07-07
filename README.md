
# 📱 Social Messaging App - API REST

Este proyecto implementa una API REST para una aplicación de mensajería social utilizando Spring Boot y base de datos Oracle.

---

## 🚀 Configuración Rápida

### Prerrequisitos
- Oracle Database 21c o superior instalado y funcionando
- Java 17 o superior
- Maven

### Configurar Oracle Database

1. **Instalar Oracle Database** (si no está instalado)
   - Descargar Oracle Database Express Edition (XE) desde [Oracle.com](https://www.oracle.com/database/technologies/xe-downloads.html)
   - Instalar siguiendo las instrucciones oficiales

2. **Crear Usuario y Esquema**
   ```sql
   -- Conectar como SYSDBA
   CREATE USER SOCIAL_UD IDENTIFIED BY social2025;
   GRANT CONNECT, RESOURCE, DBA TO SOCIAL_UD;
   GRANT CREATE SESSION TO SOCIAL_UD;
   GRANT UNLIMITED TABLESPACE TO SOCIAL_UD;
   ```

3. **Ejecutar Scripts de Inicialización**
   ```sql
   -- Conectar como SOCIAL_UD y ejecutar:
   @src/main/resources/01-init-database-oracle.sql
   @src/main/resources/02-sample-data-oracle.sql
   ```

### Ejecutar la Aplicación
```bash
# Compilar y ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:9091`

---

## 📚 Endpoints de la API

| Método | Endpoint                                                  | Descripción                                      |
|--------|-----------------------------------------------------------|--------------------------------------------------|
| `GET`  | `/api/users`                                              | Obtener todos los usuarios                       |
| `GET`  | `/api/users/login/{iduser}`                               | Consultar usuario por ID (login)                 |
| `POST` | `/api/users/register`                                     | Registrar un nuevo usuario                       |
| `GET`  | `/api/users/user/{id}/chats`                              | Obtener lista de chats del usuario               |
| `GET`  | `/api/messages/private?userId1=...&userId2=...`           | Obtener mensajes privados entre dos usuarios     |
| `GET`  | `/api/messages/group/{groupId}`                           | Obtener mensajes dentro de un grupo              |
| `POST` | `/api/messages`                                           | Enviar un mensaje privado o grupal               |

---

## 📤 JSONs de ejemplo

### 🔹 Registro de usuario – `POST /api/users/register`

```json
{
  "userId": "00003",
  "userName": "Laura",
  "userLastName": "Mejía",
  "userUniqueName": "lmejia",
  "email": "laura@example.com",
  "phone": "3115551234",
  "registrationDate": "2025-07-07",
  "locationCode": "C001"
}
```

---

### 🔹 Enviar mensaje privado – `POST /api/messages`

```json
{
  "senderUserId": "00001",
  "receiverUserId": "00002",
  "groupId": null,
  "parentMessageId": null,
  "content": "Hola, ¿cómo estás?",
  "contentTypeName": "Texto"
}
```

---

### 🔹 Enviar mensaje grupal – `POST /api/messages`

```json
{
  "senderUserId": "00001",
  "receiverUserId": null,
  "groupId": 1,
  "parentMessageId": null,
  "content": "¡Hola equipo del grupo!",
  "contentTypeName": "Texto"
}
```

---

## 🗄️ Configuración de Base de Datos

### Conexión Oracle
- **URL**: `jdbc:oracle:thin:@localhost:1521:XE`
- **Usuario**: `SOCIAL_UD`
- **Contraseña**: `social2025`
- **Puerto**: `1521`
- **Servicio**: `XE`

### Herramientas de Gestión
- **Oracle SQL Developer**: Herramienta gráfica oficial de Oracle
- **SQL*Plus**: Cliente de línea de comandos incluido con Oracle
- **Oracle Enterprise Manager**: Interfaz web de administración

### Comandos Útiles
```bash
# Conectar con SQL*Plus
sqlplus SOCIAL_UD/social2025@//localhost:1521/XE

# Ejecutar scripts desde SQL*Plus
@src/main/resources/01-init-database-oracle.sql
@src/main/resources/02-sample-data-oracle.sql
```

---

## 🔧 Troubleshooting

### Error de Conexión
- Verificar que Oracle esté ejecutándose: `lsnrctl status`
- Verificar que el servicio XE esté activo
- Comprobar credenciales en `application.properties`
- Verificar que el usuario `SOCIAL_UD` tenga permisos adecuados

### Error de Scripts
- Ejecutar scripts como usuario `SOCIAL_UD`
- Verificar que las secuencias se creen correctamente
- Verificar que las tablas se creen en el esquema correcto



