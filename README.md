# Stocky - Sistema de control de inventario para pymes
Proyecto academico desarrollado con **Java Spring Boot** para la asignatura Desarrollo Fullstack I.
## Sobre Stocky
**Stocky** es una app web orientada a pequeñas y medianas empresas para gestionar eficientemente su inventario facilitando procesos como:

- Registro y actualización de productos
- Contol sobre stock
- Consultas sobre historial de movimientos
- Ver proveedores de productos
## Tecnologías en las que esta construido
![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/maven-4.0.0-green)
![Sring](https://img.shields.io/badge/Spring-3.5.0-light_green)
## Requisitos
- **Java 21** o superior
- **Laragon**
- **HediSQL**
- **IDE Recomendado:** Vs Code | IntelliJ IDEA Eclipse 

## Cómo Ejecutar
### 1) Clonar el repositorio
```
git clone https://github.com/vicentecuritol/Stocky
```
### 2) Encerder **Laragon**
Levantar servicio de **MySQL** en `localHost:3306`
### 3) Ejecutar programa
- Mediante Powershell / CMD de Windows en carpeta raiz de stocky
```bash
    .\mvnw.cmd spring-boot:run
```
- O ejecutar directamente de un IDE en:
```bash
src/main/java/com/cavi/stocky/StockyApplication.java
``` 
### 4) En la ventana emergente de **HediSQL** ingresar como:
#### Usuario:
```
root 
```
#### Contraseña
```
System 
```
### 5) Crear una nueva Base de datos
```sql
CREATE DATABASE stocky_db;
```
`Las tablas son construidas automaticamente`

## Autores
- Vicente Curitol
- Catalina Vega
