package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProveedorResponseDto;
import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.service.ProveedorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
// controller de proveedor, mismo patron que CategoriaController
@RestController
@RequestMapping("/api/v1/proveedores")
@AllArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    // GET /api/v1/proveedores - trae todos los proveedores
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDto>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorService.getProveedores();
        List<ProveedorResponseDto> respuestas = proveedores.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    // GET /api/v1/proveedores/{id} - busca un proveedor por id
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> obtenerPorId(@PathVariable Long id) {
        Proveedor proveedor = proveedorService.getProveedorId(id);
        if (proveedor != null) {
            return ResponseEntity.ok(convertirAResponse(proveedor));
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/v1/proveedores - crea un proveedor nuevo
    @PostMapping
    public ResponseEntity<ProveedorResponseDto> crear(@Valid @RequestBody Proveedor proveedor) {
        Proveedor nuevo = proveedorService.saveProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevo));
    }

    // PUT /api/v1/proveedores/{id} - actualiza un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        proveedor.setId(id);
        Proveedor actualizado = proveedorService.updateProveedor(proveedor);
        if (actualizado != null) {
            return ResponseEntity.ok(convertirAResponse(actualizado));
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/v1/proveedores/{id} - elimina un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (proveedorService.existeProveedor(id)) {
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // convierte Proveedor al DTO de respuesta
    private ProveedorResponseDto convertirAResponse(Proveedor proveedor) {
        return new ProveedorResponseDto(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getEmail(),
                proveedor.getTelefono()
        );
    }
}