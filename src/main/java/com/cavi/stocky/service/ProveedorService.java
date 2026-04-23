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

    public List<Proveedor> getProveedores(){
        return proveedorRepository.findAll();
    }

    public Proveedor saveProveedores(Proveedor prov){
        return proveedorRepository.save(prov);
    }

    public Proveedor getProveedorId (Long id){
        return proveedorRepository.findById(id).orElse(null);
    }

    public Proveedor updateProvedores(Proveedor prov){
        if(!proveedorRepository.existsById(prov.getId())){
            return null;
        }
        return proveedorRepository.save(prov);
    }

    public void deleteProveedor (Long id){
        proveedorRepository.deleteById(id);
    }
}
