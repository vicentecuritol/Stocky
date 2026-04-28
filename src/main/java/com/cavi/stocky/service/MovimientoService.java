package com.cavi.stocky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.repository.MovimientoRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    // Obtener todos los movimientos
    public List<Movimiento> getMovimientos() {
        return movimientoRepository.findAll();
    }

    // Obtener un movimiento por ID
    public Movimiento getMovimientoId(Long id) {
        return movimientoRepository.findById(id).orElse(null);
    }

    // Guardar un nuevo movimiento
    public Movimiento saveMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    // Actualizar un movimiento
    public Movimiento updateMovimiento(Movimiento movimiento) {
        if (!movimientoRepository.existsById(movimiento.getId())) {
            return null;
        }
        return movimientoRepository.save(movimiento);
    }

    // Eliminar un movimiento
    public void eliminarMovimiento(Long id) {
        if (movimientoRepository.existsById(id)) {
            movimientoRepository.deleteById(id);
        }
    }

    // Verificar si existe un movimiento
    public boolean existeMovimiento(Long id) {
        return movimientoRepository.existsById(id);
    }
}

