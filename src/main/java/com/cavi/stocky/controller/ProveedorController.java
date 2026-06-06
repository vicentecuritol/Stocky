package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProveedorCreateRequestDto;
import com.cavi.stocky.dto.ProveedorResponseDto;
import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.service.ProveedorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
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
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    // GET /api/v1/proveedores/{id} - busca un proveedor por id
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirAResponse(proveedorService.getProveedorId(id)));
    }

    // POST /api/v1/proveedores - crea un proveedor nuevo
    @PostMapping
    public ResponseEntity<ProveedorResponseDto> crear(@Valid @RequestBody ProveedorCreateRequestDto request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());
        Proveedor nuevo = proveedorService.saveProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevo));
    }

    // PUT /api/v1/proveedores/{id} - actualiza un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorCreateRequestDto request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(id);
        proveedor.setNombre(request.getNombre());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());
        return ResponseEntity.ok(convertirAResponse(proveedorService.updateProveedor(proveedor)));
    }

    // DELETE /api/v1/proveedores/{id} - elimina un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
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