package com.cavi.stocky.service;

import com.cavi.stocky.exception.ResourceNotFoundException;
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

// tests unitarios de ProductoService
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private ProductoService productoService;

    // --- tests de eliminarProducto ---

    @Test
    void eliminar_producto_con_movimientos_asociados_lanza_excepcion() {
        // simulamos que el producto existe
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // simulamos que el producto tiene movimientos asociados
        when(movimientoRepository.existsByProductoId(1L)).thenReturn(true);

        // debe lanzar excepcion y no eliminar nada
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productoService.eliminarProducto(1L));

        assertEquals("No se puede eliminar el producto porque tiene movimientos asociados",
                ex.getMessage());
        // verificamos que deleteById nunca se llamo
        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void eliminar_producto_sin_movimientos_elimina_correctamente() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // el producto no tiene movimientos
        when(movimientoRepository.existsByProductoId(1L)).thenReturn(false);

        productoService.eliminarProducto(1L);

        // verificamos que si se llamo a deleteById
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminar_producto_inexistente_lanza_excepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.eliminarProducto(99L));
    }

    // --- tests de getProductoId ---

    @Test
    void buscar_producto_por_id_existente_retorna_producto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.getProductoId(1L);

        assertEquals("Arroz", resultado.getNombre());
    }

    @Test
    void buscar_producto_por_id_inexistente_lanza_excepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.getProductoId(99L));
    }
}
