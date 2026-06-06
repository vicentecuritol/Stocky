package com.cavi.stocky.service;

import com.cavi.stocky.exception.ResourceNotFoundException;
import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.repository.CategoriaRepository;
import com.cavi.stocky.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// tests unitarios de CategoriaService
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    // --- tests de eliminarCategoria ---

    @Test
    void eliminar_categoria_con_productos_asociados_lanza_excepcion() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // la categoria tiene productos asociados
        when(productoRepository.existsByCategoriaId(1L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> categoriaService.eliminarCategoria(1L));

        assertEquals("No se puede eliminar la categoría porque tiene productos asociados",
                ex.getMessage());
        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    void eliminar_categoria_sin_productos_elimina_correctamente() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // no hay productos asociados
        when(productoRepository.existsByCategoriaId(1L)).thenReturn(false);

        categoriaService.eliminarCategoria(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void eliminar_categoria_inexistente_lanza_excepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.eliminarCategoria(99L));
    }

    // --- tests de getCategoriaByNombre ---

    @Test
    void buscar_categoria_por_nombre_existente_retorna_categoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Abarrotes");
        when(categoriaRepository.findByNombreIgnoreCase("abarrotes"))
                .thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.getCategoriaByNombre("abarrotes");

        assertEquals("Abarrotes", resultado.getNombre());
    }

    @Test
    void buscar_categoria_por_nombre_inexistente_lanza_excepcion() {
        when(categoriaRepository.findByNombreIgnoreCase("NoExiste"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.getCategoriaByNombre("NoExiste"));
    }

    // --- tests de getCategoriaId ---

    @Test
    void buscar_categoria_por_id_inexistente_lanza_excepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.getCategoriaId(99L));
    }
}
