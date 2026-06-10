package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ProductoCreateRequestDto;
import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.model.Producto;
import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.service.CategoriaService;
import com.cavi.stocky.service.ProductoService;
import com.cavi.stocky.service.ProveedorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {
    @Mock
    private ProductoService productoService;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    void crearProducto_retorna201_cuandoExisteProveedorYCategoria(){
        //Aqui vamos a verificar que el metodo agregarProducto del controlador funciona correctamente
        //Para ello crearemos un producto con un proveedor valido, tambien una categoria valida y simularemos el comportamiento del servicio
        // Preparamos los datos: una categoría que existe en la BD
        Categoria categoria = new Categoria(1L, "Electrónica", "Producto electronico");

        // Preparamos el proveedor
        Proveedor proveedor = new Proveedor(1L, "Tech Supply", "contact@techsupply.com", "555-1234");

        // Creamos el DTO con los datos que envía el cliente
        ProductoCreateRequestDto request = new ProductoCreateRequestDto();
        request.setNombre("Laptop ASUS");
        request.setPrecio(1200);
        request.setStockActual(5);
        request.setStockMinimo(2);
        request.setCategoriaNombre("Electrónica");
        request.setProveedorNombre("Tech Supply");
        request.setProveedorEmail("contact@techsupply.com");

        // Simulamos que el servicio de categoría encuentra la categoría por nombre
        when(categoriaService.getCategoriaByNombre("Electrónica")).thenReturn(categoria);

        // Simulamos que el servicio de proveedor encuentra el proveedor por nombre
        when(proveedorService.getProveedorByNombre("Tech Supply")).thenReturn(proveedor);

        // Creamos el producto esperado que devolverá el servicio
        Producto productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Laptop ASUS");
        productoGuardado.setPrecio(1200);
        productoGuardado.setStockActual(5);
        productoGuardado.setStockMinimo(2);
        productoGuardado.setCategoria(categoria);
        productoGuardado.setProveedor(proveedor);

        // Simulamos que el servicio guarda el producto
        when(productoService.saveProducto(org.mockito.ArgumentMatchers.any(Producto.class)))
                .thenReturn(productoGuardado);

        // Llamamos al método del controlador
        var respuesta = productoController.crear(request);

        // Verificaciones
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        var body = respuesta.getBody();
        Assertions.assertNotNull(body);
        assertNotNull(body);
        assertEquals("Laptop ASUS", body.getNombre());
        assertEquals("Electrónica", body.getCategoriaNombre());
    }

    /**
     * Test 2: Crear un nuevo PROVEEDOR exitosamente
     * Verifica que al crear un producto con un proveedor válido,
     * se valide correctamente el email del proveedor
     */
    @Test
    void crearProducto_retorna201_conProveedorValido() {
        // Preparamos los datos
        Categoria categoria = new Categoria(2L, "Accesorios", "");
        Proveedor proveedor = new Proveedor(2L, "AccesoriosMundo", "ventas@accesorios.mx", "555-5678");

        ProductoCreateRequestDto request = new ProductoCreateRequestDto();
        request.setNombre("Mouse inalámbrico");
        request.setPrecio(25);
        request.setStockActual(50);
        request.setStockMinimo(10);
        request.setCategoriaNombre("Accesorios");
        request.setProveedorNombre("AccesoriosMundo");
        request.setProveedorEmail("ventas@accesorios.mx");

        // Simulamos los servicios
        when(categoriaService.getCategoriaByNombre("Accesorios")).thenReturn(categoria);
        when(proveedorService.getProveedorByNombre("AccesoriosMundo")).thenReturn(proveedor);

        Producto productoGuardado = new Producto();
        productoGuardado.setId(2L);
        productoGuardado.setNombre("Mouse inalámbrico");
        productoGuardado.setPrecio(25);
        productoGuardado.setStockActual(50);
        productoGuardado.setStockMinimo(10);
        productoGuardado.setCategoria(categoria);
        productoGuardado.setProveedor(proveedor);

        when(productoService.saveProducto(org.mockito.ArgumentMatchers.any(Producto.class)))
                .thenReturn(productoGuardado);

        // Llamamos al controlador
        var respuesta = productoController.crear(request);

        // Verificaciones
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        var body = respuesta.getBody();
        Assertions.assertNotNull(body);
        assertNotNull(body);
        assertEquals("Mouse inalámbrico", body.getNombre());
        assertEquals(25, body.getPrecio());
    }

    /**
     * Test 3: Crear un nuevo PRODUCTO exitosamente
     * Verifica el flujo completo: categoría + proveedor + producto
     */
    @Test
    void crearProducto_retorna201_conProductoCompleto() {
        // Preparamos los datos: categoría, proveedor y producto
        Categoria categoria = new Categoria(3L, "Monitores", "");
        Proveedor proveedor = new Proveedor(3L, "MonitorPro", "info@monitorpro.com", "555-9999");

        ProductoCreateRequestDto request = new ProductoCreateRequestDto();
        request.setNombre("Monitor LG 27\" 4K");
        request.setPrecio(350);
        request.setStockActual(12);
        request.setStockMinimo(5);
        request.setCategoriaNombre("Monitores");
        request.setProveedorNombre("MonitorPro");
        request.setProveedorEmail("info@monitorpro.com");

        // Simulamos los servicios
        when(categoriaService.getCategoriaByNombre("Monitores")).thenReturn(categoria);
        when(proveedorService.getProveedorByNombre("MonitorPro")).thenReturn(proveedor);

        Producto productoGuardado = new Producto();
        productoGuardado.setId(3L);
        productoGuardado.setNombre("Monitor LG 27\" 4K");
        productoGuardado.setPrecio(350);
        productoGuardado.setStockActual(12);
        productoGuardado.setStockMinimo(5);
        productoGuardado.setCategoria(categoria);
        productoGuardado.setProveedor(proveedor);

        when(productoService.saveProducto(org.mockito.ArgumentMatchers.any(Producto.class)))
                .thenReturn(productoGuardado);

        // Llamamos al controlador
        var respuesta = productoController.crear(request);

        // Verificaciones completas
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        var body = respuesta.getBody();
        Assertions.assertNotNull(body);
        assertNotNull(body);
        assertEquals("Monitor LG 27\" 4K", body.getNombre());
        assertEquals(350, body.getPrecio());
        assertEquals(12, body.getStockActual());
        assertEquals(5, body.getStockMinimo());
        assertEquals("Monitores", body.getCategoriaNombre());
    }
}


