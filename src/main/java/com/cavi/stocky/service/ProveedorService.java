package com.cavi.stocky.service;

import java.util.List;

import com.cavi.stocky.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.repository.ProveedorRepository;


// logica de negocio de proveedor, mismo patron que CategoriaService
@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // retorna todos los proveedores registrados
    public List<Proveedor> getProveedores() {
        return proveedorRepository.findAll();
    }

    // busca un proveedor por id, devuelve proveedor no encotrado con id cuando no se encuentra un id del proveedor
    public Proveedor getProveedorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " +id));
    }

    // Guardar un nuevo proovedor en en la base de datos
    public Proveedor saveProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // actualiza un proveedor existente, si no existe devuelve null
    public Proveedor updateProveedor(Proveedor proveedor) {
        proveedorRepository.findById(proveedor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + proveedor.getId()));
        return proveedorRepository.save(proveedor);
    }

    // elimina un proveedor si existe
    public void eliminarProveedor(Long id) {
      proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id" + id));
        proveedorRepository.deleteById(id);
    }

    //Esto es para buscar el proveedor por el nombre
    public Proveedor getProveedorByNombre(String nombre) {
        return proveedorRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + nombre));
    }
}