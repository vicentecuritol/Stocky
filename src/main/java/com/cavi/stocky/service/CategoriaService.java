package com.cavi.stocky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }

    public Categoria saveCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public Categoria getCategoriaId(Long id){
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria updateCategoria(Categoria categoria){
        if(!categoriaRepository.existsById(categoria.getId())){
            return null;
        }
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id){
        categoriaRepository.deleteById(id);
    }
}
