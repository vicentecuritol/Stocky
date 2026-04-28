package com.cavi.stocky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Proveedor;
import com.cavi.stocky.repository.ProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // para obtener todos los proveedores
    public List<Proveedor> getProveedores() {
        return proveedorRepository.findAll();
    }

    // Obtener un proveedor a traves del id
    public Proveedor getProveedorId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    // Guardar un nuevo proovedor en en la abase de datos
    public Proveedor saveProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Actualizar un proveedor registardo
    public Proveedor updateProveedor(Proveedor proveedor) {
        if (!proveedorRepository.existsById(proveedor.getId())) {
            return null;
        }
        return proveedorRepository.save(proveedor);
    }

    // Eliminar un proveedo de los registrados
    public void eliminarProveedor(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
        }
    }

    // Verificar si existe un proveedor
    public boolean existeProveedor(Long id) {
        return proveedorRepository.existsById(id);
    }
}