package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.repository.MovimientoRepository;
import com.cavi.stocky.repository.ProductoRepository;

// logica de negocio de movimiento, registra entradas y salidas de stock
@Service
@AllArgsConstructor
public class MovimientoService {
    private MovimientoRepository movimientoRepository;
    private ProductoRepository productoRepository; // inyección necesaria para actualizar el stock

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
        Producto producto = movimiento.getProducto();
        if(producto != null ) {
            String tipo = movimiento.getTipo().toUpperCase();
            int stockActual =  producto.getStockActual() != null ? producto.getStockActual() : 0;
            if(tipo.equals("ENTRADA")) {
                producto.setStockActual(stockActual + movimiento.getCantidad());
            }else if(tipo.equals("SALIDA")) {
                int nuevoStock = stockActual - movimiento.getCantidad();
                if(nuevoStock < 0) {
                    throw new IllegalArgumentException("Stock insuficiente para realizar la salida");
                }
                producto.setStockActual(nuevoStock);
            }
            productoRepository.save(producto); //Aqui fue requerido inyectar producto repository
        }
        return movimientoRepository.save(movimiento);
    }



    // elimina un movimiento si existe
    public void eliminarMovimiento(Long id){
        movimientoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id:" + id));
        movimientoRepository.deleteById(id);
    }
}

