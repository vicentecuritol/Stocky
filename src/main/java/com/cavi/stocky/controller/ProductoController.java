package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProductoResponse;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.service.ProductoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    //obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<Producto> productos = productoService.getProductos();
        List<ProductoResponse> respuestas = productos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    //obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.getProductoId(id);
        if (producto != null) {
            return ResponseEntity.ok(convertirAResponse(producto));
        }
        return ResponseEntity.notFound().build();
    }

    //crear
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevoProducto));
    }

    //actualizar
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        producto.setId(id);
        Producto actualizado = productoService.updateProducto(producto);
        if (actualizado != null) {
            return ResponseEntity.ok(convertirAResponse(actualizado));
        }
        return ResponseEntity.notFound().build();
    }


    //eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.existeProducto(id)) {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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
