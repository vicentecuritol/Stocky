package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProductoCreateRequestDto;
import com.cavi.stocky.dto.ProductoResponseDto;
import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.service.CategoriaService;
import com.cavi.stocky.service.ProductoService;
import com.cavi.stocky.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.cavi.stocky.exception.ResourceNotFoundException;
// controller de producto, el mas completo porque necesita categoria y proveedor al crear
@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*", maxAge = 3600)// permite llamadas desde cualquier origen, util para el frontend
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;  // para buscar la categoria al crear producto
    private final ProveedorService proveedorService;  // para buscar el proveedor al crear producto

    // GET /api/v1/productos - trae todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> obtenerTodos() {
        List<Producto> productos = productoService.getProductos();
        List<ProductoResponseDto> respuestas = productos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    // GET /api/v1/productos/{id} - busca un producto por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirAResponse(productoService.getProductoId(id)));
    }

    // POST /api/v1/productos - crea un producto nuevo
    // recibe ProductoCreateRequestDto en vez del modelo directo para mayor control
    @PostMapping
    public ResponseEntity<ProductoResponseDto> crear(@Valid @RequestBody ProductoCreateRequestDto request) {
        // buscamos la categoria por nombre entre las registradas, si no existe lanza excepcion
        Categoria categoria = categoriaService.getCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(request.getCategoriaNombre()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada: " + request.getCategoriaNombre()));

        // buscamos el proveedor por nombre, si no existe lanza excepcion
        Proveedor proveedor = proveedorService.getProveedores().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(request.getProveedorNombre()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado: " + request.getProveedorNombre()));

        // verificamos que el email que mando el cliente coincida con el del proveedor registrado
        if (!proveedor.getEmail().equalsIgnoreCase(request.getProveedorEmail())) {
            throw new IllegalArgumentException(
                    "El email del proveedor no coincide. email registrado: " + proveedor.getEmail());
        }

        // construimos el objeto producto con todos los datos
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStockActual(request.getStockActual());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);

        Producto nuevoProducto = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevoProducto));
    }

    // PUT /api/v1/productos/{id} - actualiza un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        producto.setId(id);
        return ResponseEntity.ok(convertirAResponse(productoService.updateProducto(producto)));
    }

    // DELETE /api/v1/productos/{id} - elimina un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/productos/bajo-stock - productos que necesitan reabastecimiento
    // retorna los que tienen stockActual menor o igual al stockMinimo
    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoResponseDto>>
    productosBajoStock() {
        List<ProductoResponseDto> bajoStock = productoService.getProductosBajoStock().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bajoStock);
    }

    // convierte Producto al DTO, muestra nombres de categoria y proveedor en vez de objetos completos
    private ProductoResponseDto convertirAResponse(Producto producto) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getProveedor() != null ? producto.getProveedor().getNombre() : null
        );
    }
}


