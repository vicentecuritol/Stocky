package com.cavi.stocky.controller;

import com.cavi.stocky.dto.MovimientoResponse;
import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.service.MovimientoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movimientos")
@AllArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // Obtiene todos los moviminetps existentes
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        List<Movimiento> movimientos = movimientoService.getMovimientos();
        List<MovimientoResponse> respuestas = movimientos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    // Obtiene un movimiento por id
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.getMovimientoId(id);
        if (movimiento != null) {
            return ResponseEntity.ok(convertirAResponse(movimiento));
        }
        return ResponseEntity.notFound().build();
    }

    // Crea un nuevo movimiento
    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(@Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevo = movimientoService.saveMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevo));
    }

    // Actualiza el movimiento
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody Movimiento movimiento) {
        movimiento.setId(id);
        Movimiento actualizado = movimientoService.updateMovimiento(movimiento);
        if (actualizado != null) {
            return ResponseEntity.ok(convertirAResponse(actualizado));
        }
        return ResponseEntity.notFound().build();
    }

    // Elimina el movimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (movimientoService.existeMovimiento(id)) {
            movimientoService.eliminarMovimiento(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovimientoResponse convertirAResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getObservacion(),
                movimiento.getProducto() != null ? movimiento.getProducto().getNombre() : null
        );
    }
}
