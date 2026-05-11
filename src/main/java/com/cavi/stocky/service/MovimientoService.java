package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.repository.MovimientoRepository;

// logica de negocio de movimiento, registra entradas y salidas de stock
@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    // retorna todo el historial de movimientos
    public List<Movimiento> getMovimientos() {
        return movimientoRepository.findAll();
    }

    // busca un movimiento por id, devuelve un manejo desde Resource cuando no se encuentra un movimiento
    public Movimiento getMovimientoId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: "+ id));
    }

    // guarda un movimiento nuevo en el historial
    public Movimiento saveMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    // actualiza un movimiento existente, verifica que exista antes de guardar
    public Movimiento updateMovimiento(Movimiento movimiento) {
        if (!movimientoRepository.existsById(movimiento.getId())) {
            return null;
        }
        return movimientoRepository.save(movimiento);
    }

    // elimina un movimiento si existe
    public void eliminarMovimiento(Long id) {
        if (movimientoRepository.existsById(id)) {
            movimientoRepository.deleteById(id);
        }
    }

    // Verificar si existe un movimiento con ese id
    public boolean existeMovimiento(Long id) {
        return movimientoRepository.existsById(id);
    }
}

