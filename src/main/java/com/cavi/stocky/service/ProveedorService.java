package com.cavi.stocky.service;

import java.util.List;

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

    // busca un proveedor por id, devuelve null si no existe
    public Proveedor getProveedorId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    // Guardar un nuevo proovedor en en la base de datos
    public Proveedor saveProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // actualiza un proveedor existente, si no existe devuelve null
    public Proveedor updateProveedor(Proveedor proveedor) {
        if (!proveedorRepository.existsById(proveedor.getId())) {
            return null;
        }
        return proveedorRepository.save(proveedor);
    }

    // elimina un proveedor si existe
    public void eliminarProveedor(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
        }
    }

    // Verificar si existe un proveedor con ese id
    public boolean existeProveedor(Long id) {
        return proveedorRepository.existsById(id);
    }
}