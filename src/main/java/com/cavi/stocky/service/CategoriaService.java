package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import com.cavi.stocky.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.repository.CategoriaRepository;

// logica de negocio de categoria
// el controller recibe la peticion HTTP y nos la pasa, nosotros hacemos el trabajo
@Service // indica que esta clase es un componente de logica de negocio
@AllArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    // retorna todas las categorias, si no hay ninguna devuelve lista vacia
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    // busca por id, donde si no se encuentra la categoria se va con la excepcion ResourseNotFounfException
    public Categoria getCategoriaId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    // guarda una categoria nueva y la retorna con el id asignado por la BD
    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // actualiza una categoria, verifica que exista antes de guardar
    public Categoria updateCategoria(Categoria categoria) {
        categoriaRepository.findById(categoria.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no enncontrada con id: " + categoria.getId()));
        return categoriaRepository.save(categoria);// si no existe devolvemos null y el controller responde 404
    }

    // elimina la categoria con ese id
    public void eliminarCategoria(Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        if (productoRepository.existsByCategoriaId(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.deleteById(id);
    }

    //Esto es para buscar una categoria
    public Categoria getCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + nombre));
    }
}
