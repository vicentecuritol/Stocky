package com.cavi.stocky.controller;

import com.cavi.stocky.dto.MovimientoCreateRequestDto;
import com.cavi.stocky.dto.MovimientoResponseDto;
import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.service.MovimientoService;
import com.cavi.stocky.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
// controller de movimiento, maneja el historial de entradas y salidas de stock
@RestController
@RequestMapping("/api/v1/movimientos")
@AllArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final ProductoService productoService; // campo faltante

    // GET /api/v1/movimientos - trae todo el historial
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDto>> obtenerTodos() {
        List<Movimiento> movimientos = movimientoService.getMovimientos();
        List<MovimientoResponseDto> respuestas = movimientos.stream()
                .map(this::convertirAResponse)
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    // GET /api/v1/movimientos/{id} - busca un movimiento por id
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirAResponse(movimientoService.getMovimientoId(id)));
    }

    // POST /api/v1/movimientos - registra una entrada o salida de stock
    @PostMapping
    public ResponseEntity<MovimientoResponseDto> crear(@Valid @RequestBody MovimientoCreateRequestDto request) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(request.getTipo());
        movimiento.setCantidad(request.getCantidad());
        movimiento.setFecha(request.getFecha());
        movimiento.setObservacion(request.getObservacion());
        movimiento.setProducto(productoService.getProductoId(request.getProductoId())); // p minúscula — instancia

        Movimiento nuevo = movimientoService.saveMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nuevo));
    }


    // DELETE /api/v1/movimientos/{id} - elimina un movimiento del historial
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    // convierte Movimiento al DTO, muestra el nombre del producto en vez del objeto completo
    private MovimientoResponseDto convertirAResponse(Movimiento movimiento) {
        return new MovimientoResponseDto(
                movimiento.getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getObservacion(),
                movimiento.getProducto() != null ? movimiento.getProducto().getNombre() : null
        );
    }
}
