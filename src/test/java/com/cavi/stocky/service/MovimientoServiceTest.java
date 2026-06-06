package com.cavi.stocky.service;

import com.cavi.stocky.exception.ResourceNotFoundException;
import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.repository.MovimientoRepository;
import com.cavi.stocky.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// tests unitarios de MovimientoService
// usamos Mockito para simular los repositorios sin necesitar base de datos real
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository; // repositorio simulado

    @Mock
    private ProductoRepository productoRepository; // repositorio simulado

    @InjectMocks
    private MovimientoService movimientoService; // el service real con los mocks inyectados

    // --- tests de saveMovimiento ---

    @Test
    void entrada_suma_stock_al_producto() {
        // preparamos un producto con 10 unidades en stock
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockActual(10);

        // preparamos un movimiento de ENTRADA de 5 unidades
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("ENTRADA");
        movimiento.setCantidad(5);
        movimiento.setProducto(producto);

        // simulamos que el repositorio guarda el movimiento
        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.saveMovimiento(movimiento);

        // verificamos que el stock quedo en 15 (10 + 5)
        assertEquals(15, producto.getStockActual());
        // verificamos que se llamo a guardar el producto con el nuevo stock
        verify(productoRepository).save(producto);
    }

    @Test
    void salida_resta_stock_al_producto() {
        // preparamos un producto con 10 unidades
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockActual(10);

        // preparamos un movimiento de SALIDA de 4 unidades
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("SALIDA");
        movimiento.setCantidad(4);
        movimiento.setProducto(producto);

        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.saveMovimiento(movimiento);

        // verificamos que el stock quedo en 6 (10 - 4)
        assertEquals(6, producto.getStockActual());
        verify(productoRepository).save(producto);
    }

    @Test
    void salida_con_stock_insuficiente_lanza_excepcion() {
        // producto con solo 3 unidades
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockActual(3);

        // intentamos sacar 10, mas de lo que hay
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("SALIDA");
        movimiento.setCantidad(10);
        movimiento.setProducto(producto);

        // debe lanzar IllegalArgumentException con el mensaje de stock insuficiente
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> movimientoService.saveMovimiento(movimiento));

        assertEquals("Stock insuficiente para realizar la salida", ex.getMessage());
        // verificamos que NO se guardo nada en la base de datos
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void tipo_minusculas_funciona_igual_que_mayusculas() {
        // el tipo viene en minusculas desde el cliente
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockActual(10);

        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("entrada"); // minusculas
        movimiento.setCantidad(5);
        movimiento.setProducto(producto);

        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.saveMovimiento(movimiento);

        // el service hace toUpperCase, asi que debe funcionar igual
        assertEquals(15, producto.getStockActual());
    }

    @Test
    void movimiento_sin_producto_se_guarda_sin_modificar_stock() {
        // movimiento sin producto asociado
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("ENTRADA");
        movimiento.setCantidad(5);
        movimiento.setProducto(null); // sin producto

        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.saveMovimiento(movimiento);

        // si no hay producto, no debe intentar guardar en productoRepository
        verify(productoRepository, never()).save(any());
    }

    // --- tests de eliminarMovimiento ---

    @Test
    void eliminar_movimiento_inexistente_lanza_excepcion() {
        when(movimientoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> movimientoService.eliminarMovimiento(99L));
    }
}
