package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Producto;
import com.cavi.stocky.repository.ProductoRepository;
// logica de negocio de producto
@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    // retorna todos los productos del inventario
    public List<Producto> getProductos(){
        return productoRepository.findAll();
    }

    // guarda un producto nuevo y lo retorna con el id asignado
    public Producto saveProducto(Producto pro){
        return productoRepository.save(pro);
    }

    // busca un producto por id, devuelve que el producto no fue encontrado llamando a ResourceNotFoundException
    public Producto getProductoId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    // actualiza un producto existente, verifica que exista antes de guardar
    public Producto updateProducto(Producto pro){
        if(!productoRepository.existsById(pro.getId())){
            return null;// no existe, el controller respondera 404
        }
        return productoRepository.save(pro);
    }

    // elimina un producto del inventario
    public void eliminarProducto(Long id){
        productoRepository.deleteById(id);
    }

    // verifica si existe un producto con ese id
    public boolean existeProducto(Long id) {
        return productoRepository.existsById(id);
    }
}
