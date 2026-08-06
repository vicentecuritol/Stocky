# Stocky — API de gestión de inventario para pymes

Stocky es una API REST desarrollada con **Java y Spring Boot** para administrar el inventario de una pequeña o mediana empresa. Centraliza productos, categorías, proveedores y movimientos de inventario, además de incorporar autenticación basada en JWT, control de permisos por rol y una consulta de clima mediante Open-Meteo.

> Este README describe el comportamiento implementado actualmente en el código fuente. Está pensado como guía para retomar el proyecto rápidamente.

## Índice

- [Qué resuelve](#qué-resuelve)
- [Arquitectura y recorrido de una petición](#arquitectura-y-recorrido-de-una-petición)
- [Modelo de dominio](#modelo-de-dominio)
- [Reglas de negocio importantes](#reglas-de-negocio-importantes)
- [Autenticación y autorización](#autenticación-y-autorización)
- [Endpoints](#endpoints)
- [Ejecución local](#ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Pruebas y documentación interactiva](#pruebas-y-documentación-interactiva)

## Qué resuelve

La API permite:

- Registrar y administrar **categorías**.
- Registrar y administrar **proveedores**.
- Crear, consultar, actualizar y eliminar **productos**.
- Detectar productos cuyo stock actual está igual o por debajo del stock mínimo.
- Registrar **entradas** y **salidas** de inventario, dejando un historial.
- Evitar salidas que dejen el stock en un valor negativo.
- Registrar usuarios e iniciar sesión para obtener un token JWT.
- Distinguir permisos de lectura (`USER` y `ADMIN`) y escritura (`ADMIN`).
- Consultar clima actual por coordenadas o mediante un acceso directo a Santiago.

## Arquitectura y recorrido de una petición

El proyecto sigue una separación por capas:

```text
Cliente HTTP
    ↓
Controller       Define rutas, recibe/valida DTOs y forma respuestas HTTP
    ↓
Service          Contiene las reglas de negocio
    ↓
Repository       Acceso a datos mediante Spring Data JPA
    ↓
MySQL            Persistencia de entidades
```

Además, una petición protegida pasa antes por la capa de seguridad:

```text
Authorization: Bearer <JWT>
    ↓
JwtFilter valida el token
    ↓
SecurityConfig identifica el rol y autoriza la operación
    ↓
Controller correspondiente
```

Los DTOs evitan exponer directamente las entidades JPA y reducen el acoplamiento entre la API pública y el modelo de persistencia. Las excepciones se concentran en un manejador global para devolver errores HTTP de forma uniforme.

## Modelo de dominio

| Entidad | Responsabilidad | Relaciones principales |
|---|---|---|
| `Producto` | Artículo inventariable: nombre, precio, stock actual y stock mínimo. | Pertenece a una `Categoria` y a un `Proveedor`. |
| `Categoria` | Clasifica productos. | Puede estar asociada a productos. |
| `Proveedor` | Representa al proveedor de productos. | Puede estar asociado a productos. |
| `Movimiento` | Registro histórico de una entrada o salida. | Está asociado a un `Producto`. |
| `Usuario` | Credenciales y rol de acceso. | Rol `ROLE_USER` o `ROLE_ADMIN`. |

### Relación funcional

```text
Categoria ──┐
            ├── Producto ── Movimiento
Proveedor ──┘
```

Un producto se crea seleccionando categoría y proveedor existentes. Los movimientos no guardan el producto completo en la respuesta: devuelven su nombre para mantener una salida más simple.

## Reglas de negocio importantes

### Creación y actualización de productos

Al crear o actualizar un producto, la API:

1. Busca la categoría por su **nombre**.
2. Busca el proveedor por su **nombre**.
3. Comprueba que el email enviado coincida —sin distinguir mayúsculas/minúsculas— con el email registrado para ese proveedor.
4. Si la categoría o el proveedor no existen, la operación falla.
5. Si el email no coincide, la operación falla con un error de argumento inválido.

Por esto, antes de registrar un producto conviene crear su categoría y proveedor.

### Movimientos y actualización de stock

Cada movimiento modifica el stock del producto asociado en el mismo flujo de guardado:

| Tipo | Efecto |
|---|---|
| `ENTRADA` | Suma la cantidad al stock actual. |
| `SALIDA` | Resta la cantidad al stock actual. |

Para una salida, Stocky calcula el nuevo valor antes de persistirlo. Si el resultado es menor que cero, rechaza la operación con el mensaje `Stock insuficiente para realizar la salida`.

> **Importante:** eliminar un movimiento elimina el registro del historial, pero el servicio actual **no revierte** el ajuste de stock que ocurrió al crearlo. No se debe usar el borrado de movimientos como mecanismo de corrección de inventario; para corregir stock, registra un movimiento compensatorio.

### Alerta de bajo stock

Un producto aparece en la consulta de bajo stock cuando:

$$stockActual \leq stockMinimo$$

## Autenticación y autorización

### Registro e inicio de sesión

- Un usuario registrado mediante `POST /api/v1/auth/register` recibe el rol `ROLE_USER`.
- Las contraseñas se almacenan con **BCrypt**, no en texto plano.
- `POST /api/v1/auth/login` autentica las credenciales y devuelve un JWT válido durante 24 horas.
- El token se usa en las rutas protegidas mediante el encabezado:

```http
Authorization: Bearer <token>
```

### Usuario administrador inicial

Al iniciar la aplicación, `AdminSeeder` crea un usuario administrador solo si todavía no existe. Sus valores se pueden configurar con las propiedades `admin.username` y `admin.password`; si no se definen, el código utiliza `admin` y `admin123` como valores predeterminados.

**Cambia estas credenciales y el secreto JWT en entornos reales.**

### Matriz de permisos

| Operación | Permiso requerido |
|---|---|
| `POST /api/v1/auth/**` | Público |
| Swagger / OpenAPI | Público |
| `GET /api/v1/**` | Usuario autenticado con rol `USER` o `ADMIN` |
| `POST`, `PUT`, `DELETE /api/v1/**` | Solo `ADMIN` |

La seguridad es *stateless*: el servidor no mantiene sesiones; cada solicitud protegida debe incluir su JWT.

## Endpoints

Base local de la API: `http://localhost:8080`

> Excepto autenticación y Swagger/OpenAPI, todas las rutas requieren JWT. Las operaciones de escritura requieren además rol `ADMIN`.

### Autenticación

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Crea un usuario con rol `USER`. |
| `POST` | `/api/v1/auth/login` | Autentica y devuelve un token JWT. |

Ejemplo de inicio de sesión:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Productos

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/productos` | Lista todos los productos. |
| `GET` | `/api/v1/productos/{id}` | Obtiene un producto por ID. |
| `POST` | `/api/v1/productos` | Crea un producto. |
| `PUT` | `/api/v1/productos/{id}` | Actualiza un producto. |
| `DELETE` | `/api/v1/productos/{id}` | Elimina un producto. |
| `GET` | `/api/v1/productos/bajo-stock` | Lista productos con stock actual menor o igual al mínimo. |

La creación/actualización recibe, entre otros datos del producto, el nombre de la categoría, el nombre del proveedor y el email del proveedor. El email debe coincidir con el proveedor registrado.

### Categorías

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/categorias` | Lista categorías. |
| `GET` | `/api/v1/categorias/{id}` | Obtiene una categoría por ID. |
| `POST` | `/api/v1/categorias` | Crea una categoría. |
| `PUT` | `/api/v1/categorias/{id}` | Actualiza una categoría. |
| `DELETE` | `/api/v1/categorias/{id}` | Elimina una categoría. |

### Proveedores

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/proveedores` | Lista proveedores. |
| `GET` | `/api/v1/proveedores/{id}` | Obtiene un proveedor por ID. |
| `POST` | `/api/v1/proveedores` | Crea un proveedor. |
| `PUT` | `/api/v1/proveedores/{id}` | Actualiza un proveedor. |
| `DELETE` | `/api/v1/proveedores/{id}` | Elimina un proveedor. |

### Movimientos

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/movimientos` | Lista el historial de movimientos. |
| `GET` | `/api/v1/movimientos/{id}` | Obtiene un movimiento por ID. |
| `POST` | `/api/v1/movimientos` | Registra una `ENTRADA` o `SALIDA` y actualiza el stock. |
| `DELETE` | `/api/v1/movimientos/{id}` | Elimina solo el registro histórico; no revierte el stock. |

Un movimiento contiene el tipo, cantidad, fecha, observación y el ID del producto. Usa `ENTRADA` o `SALIDA` como tipo para que el stock sea actualizado.

### Clima

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/clima?latitud={latitud}&longitud={longitud}` | Consulta clima actual por coordenadas mediante Open-Meteo. |
| `GET` | `/api/v1/clima/santiago` | Consulta clima de Santiago usando coordenadas predefinidas. |

Ejemplo:

```text
GET /api/v1/clima?latitud=-33.45&longitud=-70.66
```

## Ejecución local

### Requisitos

- Java 21 o superior.
- MySQL en ejecución.
- Maven, o los wrappers `mvnw` / `mvnw.cmd` incluidos en el proyecto.

### Pasos

1. Clona el repositorio:

```bash
git clone https://github.com/vicentecuritol/Stocky
cd Stocky
```

2. Crea y configura una base de datos MySQL según los valores definidos en `src/main/resources/application.properties`.

3. Define mediante variables de entorno o en `application.properties` los valores de conexión, el secreto JWT y las credenciales administrativas que correspondan a tu entorno.

4. Inicia la aplicación.

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Las tablas se administran mediante JPA/Hibernate según la propiedad `spring.jpa.hibernate.ddl-auto` configurada para el entorno.

## Ejecución con Docker

El proyecto incluye un `Dockerfile` multi-stage y un `docker-compose.yml`.

- La etapa de compilación usa Maven con Java 21 y genera el JAR sin ejecutar pruebas.
- La imagen final usa un JRE 21 y expone el puerto `8080`.
- Docker Compose levanta MySQL 8.4 y la API.
- MySQL persiste sus datos en el volumen `mysql_data`.
- El puerto `3307` del host se mapea al `3306` del contenedor MySQL.

Para levantar los servicios:

```bash
docker compose up --build
```

Para detenerlos:

```bash
docker compose down
```

Para eliminar también el volumen de datos:

```bash
docker compose down -v
```

Las variables de conexión, el secreto JWT y las credenciales del administrador están definidas actualmente en `docker-compose.yml`. Antes de publicar el proyecto o compartirlo, muévelas a un archivo de variables de entorno no versionado y reemplaza los valores por secretos seguros.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/com/cavi/stocky/
│   │   ├── config/        # Seeder de administrador y configuración OpenAPI
│   │   ├── controller/    # Endpoints REST
│   │   ├── dto/           # Objetos de entrada y respuesta de la API
│   │   ├── exception/     # Excepciones y manejador global de errores
│   │   ├── model/         # Entidades JPA
│   │   ├── repository/    # Interfaces de persistencia
│   │   ├── security/      # JWT, filtro, configuración y carga de usuarios
│   │   ├── service/       # Reglas de negocio
│   │   └── StockyApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/cavi/stocky/
        └── controller/    # Pruebas de productos y movimientos
```

## Pruebas y documentación interactiva

Hay pruebas de controlador para productos y movimientos dentro de `src/test`.

Para ejecutarlas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

La configuración de seguridad permite acceder sin token a Swagger UI y a OpenAPI. Con la aplicación iniciada, revisa la ruta configurada para Swagger, normalmente disponible desde `/swagger-ui/index.html`.

## Autores

- Vicente Curitol
- Catalina Vega
