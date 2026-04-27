package com.cavi.stocky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Producto;
import com.cavi.stocky.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto>geProductos(){
        return productoRepository.findAll();
    }

    public Producto saveProducto(Producto pro){
        return productoRepository.save(pro);
    }

    public Producto getProductoId(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public Producto updateProducto(Producto pro){
        if(!productoRepository.existsById(pro.getId())){
            return null;
        }
        return productoRepository.save(pro);
    }

    public void eliminarProducto(Long id){
        productoRepository.deleteById(id);
    }
}


