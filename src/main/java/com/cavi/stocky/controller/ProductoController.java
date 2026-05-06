package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProductoCreateRequest;
import com.cavi.stocky.dto.ProductoResponse;
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

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<Producto> productos = productoService.getProductos();
        List<ProductoResponse> respuestas = productos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.getProductoId(id);
        if (producto != null) {
            return ResponseEntity.ok(convertirAResponse(producto));
        }
        return ResponseEntity.notFound().build();
    }

    // Crear usando el DTO ProductoCreateRequest
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoCreateRequest request) {

        // Buscar y validar categoría
        Categoria categoria = categoriaService.getCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(request.getCategoriaNombre()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada: " + request.getCategoriaNombre()));

        // Buscar y validar proveedor
        Proveedor proveedor = proveedorService.getProveedores().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(request.getProveedorNombre()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado: " + request.getProveedorNombre()));

        // Validar que el email coincida
        if (!proveedor.getEmail().equalsIgnoreCase(request.getProveedorEmail())) {
            throw new IllegalArgumentException(
                    "El email del proveedor no coincide. email registrado: " + proveedor.getEmail());
        }

        // Crear el producto
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

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        producto.setId(id);
        Producto actualizado = productoService.updateProducto(producto);
        if (actualizado != null) {
            return ResponseEntity.ok(convertirAResponse(actualizado));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.existeProducto(id)) {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint especial: productos con stock bajo
    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoResponse>> productosBajoStock() {
        List<ProductoResponse> bajoStock = productoService.getProductos().stream()
                .filter(p -> p.getStockActual() <= p.getStockMinimo())
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bajoStock);
    }

    private ProductoResponse convertirAResponse(Producto producto) {
        return new ProductoResponse(
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


