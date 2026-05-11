package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.repository.CategoriaRepository;

// logica de negocio de categoria
// el controller recibe la peticion HTTP y nos la pasa, nosotros hacemos el trabajo
@Service // indica que esta clase es un componente de logica de negocio
public class CategoriaService {

    @Autowired // spring inyecta el repositorio automaticamente, no necesitamos hacer new
    private CategoriaRepository categoriaRepository;

    // retorna todas las categorias, si no hay ninguna devuelve lista vacia
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    // guarda una categoria nueva y la retorna con el id asignado por la BD
    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // busca por id, donde si no se encuentra la categoria se va con la excepcion ResourseNotFounfException
    public Categoria getCategoriaId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: "+ id));
    }

    // actualiza una categoria, verifica que exista antes de guardar
    public Categoria updateCategoria(Categoria categoria) {
        if (!categoriaRepository.existsById(categoria.getId())) {
            return null;// si no existe devolvemos null y el controller responde 404
        }
        return categoriaRepository.save(categoria); // save tambien sirve para actualizar si el id ya existe
    }

    // elimina la categoria con ese id
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    // verifica si existe una categoria con ese id, lo usamos antes de eliminar
    public boolean existeCategoria(Long id) {
        return categoriaRepository.existsById(id);
    }
}