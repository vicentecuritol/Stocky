package com.cavi.stocky.controller;

import com.cavi.stocky.dto.MovimientoCreateRequestDto;

import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.service.MovimientoService;
import com.cavi.stocky.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoControllerTest {

    @Mock
    private MovimientoService movimientoService;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private MovimientoController movimientoController;

    /**
     * Test 1: Crear ENTRADA de stock exitosamente
     * Verifica que al registrar una entrada de mercadería,
     * el controlador devuelva 201 CREATED
     */
    @Test
    void crearMovimiento_retorna201_conEntradaValida() {
        // Preparamos el producto que existe en la BD
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop ASUS");
        producto.setPrecio(1200);
        producto.setStockActual(5);
        producto.setStockMinimo(2);



        // Creamos el DTO de entrada con los datos del movimiento
        MovimientoCreateRequestDto request = new MovimientoCreateRequestDto();
        request.setTipo("ENTRADA");
        request.setCantidad(10);
        request.setFecha(LocalDateTime.now());
        request.setObservacion("Compra proveedor Tech Supply");
        request.setProductoId(1L);

        // Simulamos que el servicio de producto encuentra el producto por él, id
        when(productoService.getProductoId(1L)).thenReturn(producto);

        // Creamos el movimiento esperado que devolverá el servicio
        Movimiento movimientoGuardado = new Movimiento();
        movimientoGuardado.setId(1L);
        movimientoGuardado.setTipo("ENTRADA");
        movimientoGuardado.setCantidad(10);
        movimientoGuardado.setFecha(request.getFecha());
        movimientoGuardado.setObservacion("Compra proveedor Tech Supply");
        movimientoGuardado.setProducto(producto);

        // Simulamos que el servicio guarda el movimiento
        when(movimientoService.saveMovimiento(org.mockito.ArgumentMatchers.any(Movimiento.class)))
                .thenReturn(movimientoGuardado);

        // Llamamos al método del controlador
        var respuesta = movimientoController.crear(request);

        // Verificaciones
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        var body = respuesta.getBody();
        assertNotNull(body);
        assertEquals("ENTRADA", body.getTipo());
        assertEquals(10, body.getCantidad());
        assertEquals("Laptop ASUS", body.getProductoNombre());
    }

    /**
     * Test 2: Crear SALIDA de stock exitosamente
     * Verifica que al registrar una salida de mercadería,
     * se valide correctamente y devuelva 201 CREATED
     */
    @Test
    void crearMovimiento_retorna201_conSalidaValida() {
        // Preparamos el producto
        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Mouse inalámbrico");
        producto.setPrecio(25);
        producto.setStockActual(50);
        producto.setStockMinimo(10);

        MovimientoCreateRequestDto request = new MovimientoCreateRequestDto();
        request.setTipo("SALIDA");
        request.setCantidad(5);
        request.setFecha(LocalDateTime.now());
        request.setObservacion("Venta cliente");
        request.setProductoId(2L);

        // Simulamos los servicios
        when(productoService.getProductoId(2L)).thenReturn(producto);

        Movimiento movimientoGuardado = new Movimiento();
        movimientoGuardado.setId(2L);
        movimientoGuardado.setTipo("SALIDA");
        movimientoGuardado.setCantidad(5);
        movimientoGuardado.setFecha(request.getFecha());
        movimientoGuardado.setObservacion("Venta cliente");
        movimientoGuardado.setProducto(producto);

        when(movimientoService.saveMovimiento(org.mockito.ArgumentMatchers.any(Movimiento.class)))
                .thenReturn(movimientoGuardado);

        // Llamamos al controlador
        var respuesta = movimientoController.crear(request);

        // Verificaciones
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        var body = respuesta.getBody();
        assertNotNull(body);
        assertEquals("SALIDA", body.getTipo());
        assertEquals(5, body.getCantidad());
        assertEquals("Mouse inalámbrico", body.getProductoNombre());
    }

    /**
     * Test 3: Obtener movimiento por ID exitosamente
     * Verifica que al buscar un movimiento por ID,
     * devuelva la información correcta
     */
    @Test
    void obtenerPorId_retorna200_conMovimientoValido() {
        // Preparamos el producto
        Producto producto = new Producto();
        producto.setId(3L);
        producto.setNombre("Monitor LG 27\" 4K");
        producto.setPrecio(350);

        // Preparamos el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setTipo("ENTRADA");
        movimiento.setCantidad(8);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setObservacion("Stock inicial");
        movimiento.setProducto(producto);

        // Simulamos que el servicio encuentra el movimiento por ID
        when(movimientoService.getMovimientoId(1L)).thenReturn(movimiento);

        // Llamamos al controlador
        var respuesta = movimientoController.obtenerPorId(1L);

        // Verificaciones
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        var body = respuesta.getBody();
        assertNotNull(body);
        assertEquals(1L, body.getId());
        assertEquals("ENTRADA", body.getTipo());
        assertEquals(8, body.getCantidad());
        assertEquals("Monitor LG 27\" 4K", body.getProductoNombre());
    }
}
