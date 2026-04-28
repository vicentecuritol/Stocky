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

@RestController
@RequestMapping("/api/v1/productos")
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    //obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.geProductos());
    }

    //obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable long id) {
        Producto producto = productoService.getProductoId(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    //actualizar
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    //actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable long id, @Valid @RequestBody Producto producto) {
        producto.setId(id);
        Producto actualizado = productoService.updateProducto(producto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }


    //eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> eliminar(@PathVariable long id) {
        Producto producto = productoService.getProductoId(id);
        if (producto != null) {
            productoService.eliminarProducto(id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }


    private ProductoResponse convertirAResponse(Producto producto) {
        String categoriaNombre = producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría";
        String proveedorNombre = producto.getProveedor() != null ? producto.getProveedor().getNombre() : "Sin proveedor";

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                categoriaNombre,
                proveedorNombre
        );
    }
}