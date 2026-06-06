# Correcciones Pendientes — Proyecto Stocky

> Última revisión completa. Solo figuran correcciones aún no aplicadas.

---

## ALTO

---

### 1. `WebClientConfig.java` — Archivo que ya no se usa y debe eliminarse

**Archivo:** `config/WebClientConfig.java`

**Contexto:** Antes, `ClimaService` usaba `WebClient` para llamar a la API de Open Meteo. Ese cliente HTTP se configuraba en `WebClientConfig` como un bean de Spring. Ahora que `ClimaService` fue migrado a `RestClient`, ese bean ya no se inyecta en ningún lado del proyecto.

**Problema concreto:** Spring sigue cargando `WebClientConfig` al arrancar porque tiene `@Configuration`. Intenta crear el bean `weatherWebClient()` que ya nadie usa. Además el archivo tiene el comentario incorrecto `"cliente GTPP"`. Es código muerto que ocupa memoria y puede causar confusión.

**Corrección:** Eliminar el archivo `config/WebClientConfig.java` por completo. No requiere ningún cambio en otros archivos.

---

### 2. `MovimientoService` — Los campos del repositorio no tienen `final`

**Archivo:** `service/MovimientoService.java`  
**Líneas problemáticas:** 17-18

```java
// CÓDIGO ACTUAL — campos sin final
@Service
@AllArgsConstructor
public class MovimientoService {
    private MovimientoRepository movimientoRepository;       // falta final
    private ProductoRepository productoRepository;           // falta final
```

**Contexto:** Ya aplicaste `@AllArgsConstructor` para usar inyección por constructor, que es la práctica correcta. Sin embargo, los campos deben ser `final` para que el patrón sea completo. Sin `final`, Java permite reasignar esos campos después de la construcción, lo que puede causar bugs difíciles de detectar. Además, `@AllArgsConstructor` con campos `final` es la señal clara para otros desarrolladores de que estas dependencias son obligatorias e inmutables.

**Corrección — agregar `final` a ambos campos:**
```java
@Service
@AllArgsConstructor
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
```

---

### 3. `ProductoService` — Los campos del repositorio no tienen `final`, y tiene un import `@Autowired` sin usar

**Archivo:** `service/ProductoService.java`  
**Líneas problemáticas:** 8, 15-16

```java
// LÍNEA PROBLEMÁTICA — import innecesario que quedó de cuando usabas @Autowired
import org.springframework.beans.factory.annotation.Autowired;

// CAMPOS SIN final
private ProductoRepository productoRepository;
private MovimientoRepository movimientoRepository;
```

**Contexto:** Mismo problema que corrección 2 — ya tiene `@AllArgsConstructor` pero los campos no son `final`. Además quedó el `import` de `@Autowired` que ya no se usa, lo que genera una advertencia en el IDE.

**Corrección:**
```java
// Eliminar esta línea del import (línea 8):
import org.springframework.beans.factory.annotation.Autowired;

// Agregar final a los campos:
private final ProductoRepository productoRepository;
private final MovimientoRepository movimientoRepository;
```

---

## MEDIO

---

### 4. `CategoriaService` — Typo "enncontrada" en el mensaje de `updateCategoria`

**Archivo:** `service/CategoriaService.java`  
**Línea problemática:** 41

```java
.orElseThrow(() -> new ResourceNotFoundException("Categoria no enncontrada con id: " + categoria.getId()));
//                                                              ^^^ doble n
```

**Contexto:** Cuando alguien intenta actualizar una categoría que no existe, la API responde con este mensaje de error. Tal como está, el cliente recibe `"Categoria no enncontrada con id: 5"` con la doble `n`, lo que se ve poco profesional en la respuesta JSON.

**Corrección — cambiar solo esa línea:**
```java
.orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + categoria.getId()));
```

---

### 5. `ProveedorService` — Mensaje de `eliminarProveedor` sin separador antes del id

**Archivo:** `service/ProveedorService.java`  
**Línea problemática:** 46

```java
.orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id" + id));
// resultado en la API: "Proveedor no encontrado con id5"  ← el numero queda pegado
```

**Contexto:** Cuando se intenta eliminar un proveedor que no existe, el mensaje que recibe el cliente no tiene separación entre el texto y el número. El resultado es `"Proveedor no encontrado con id5"` en lugar de `"Proveedor no encontrado con id: 5"`. Todos los demás mensajes del proyecto sí tienen el `: ` separador.

**Corrección:**
```java
.orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
```

---

### 6. `ProveedorService` — Comentario de `saveProveedor` con doble "o" en "proovedor"

**Archivo:** `service/ProveedorService.java`  
**Línea problemática:** 30

```java
// Guardar un nuevo proovedor en la base de datos
//                     ^^^ doble o
```

**Corrección:**
```java
// Guardar un nuevo proveedor en la base de datos
```

---

## BAJO

---

### 7. Falta de tests unitarios en todo el proyecto

**Archivo:** `test/StockyApplicationTests.java`

**Contexto:** El único test existente verifica que el contexto de Spring arranque correctamente, pero no prueba ninguna lógica de negocio. Esto significa que si alguien modifica `saveMovimiento` y rompe el cálculo de stock, el error no se detecta hasta que se prueba manualmente en Postman. `spring-boot-starter-test` ya está en el `pom.xml` con JUnit 5 y Mockito incluidos — no hay que agregar nada.

**Sugerencia — archivos a crear con prioridad:**

- `MovimientoServiceTest.java` — testear que ENTRADA suma stock, SALIDA resta stock, y que SALIDA con stock insuficiente lanza `IllegalArgumentException`
- `ProductoServiceTest.java` — testear que `eliminarProducto` lanza excepción si tiene movimientos
- `CategoriaServiceTest.java` — testear que `eliminarCategoria` lanza excepción si tiene productos

---

### 8. `Collectors.toList()` desactualizado en todos los controllers

**Archivos:** `CategoriaController`, `MovimientoController`, `ProductoController`, `ProveedorController`

**Contexto:** El proyecto usa Java 21. Desde Java 16 los streams tienen el método `.toList()` directamente, sin necesidad de `.collect(Collectors.toList())`. La diferencia práctica es que `.toList()` retorna una lista inmutable (no se puede agregar ni quitar elementos después), lo que es más seguro porque evita que alguien modifique la lista de respuesta accidentalmente. Además el código queda más limpio.

**Corrección en cada controller — reemplazar todas las ocurrencias:**
```java
// ANTES
.collect(Collectors.toList());

// DESPUÉS
.toList();
```

Y eliminar el import `import java.util.stream.Collectors;` en cada archivo donde ya no se use.

---

## Resumen

| ID | Severidad | Archivo | Descripción |
|----|-----------|---------|-------------|
| 1 | 🟠 Alto | `WebClientConfig` | Archivo huérfano que ya no se usa — eliminar |
| 2 | 🟠 Alto | `MovimientoService` | Campos sin `final` con `@AllArgsConstructor` |
| 3 | 🟠 Alto | `ProductoService` | Campos sin `final` + import `@Autowired` sin usar |
| 4 | 🟡 Medio | `CategoriaService` | Typo "enncontrada" en mensaje de error |
| 5 | 🟡 Medio | `ProveedorService` | Mensaje sin `: ` antes del id |
| 6 | 🟡 Medio | `ProveedorService` | Typo "proovedor" en comentario |
| 7 | 🟢 Bajo | Tests | Sin tests unitarios de lógica de negocio |
| 8 | 🟢 Bajo | Todos los controllers | `Collectors.toList()` reemplazable por `.toList()` |

**Total: 8 correcciones — 0 críticas, 3 altas, 3 medias, 2 bajas**
