package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Proveedor;
// accede a la tabla proveedor, hereda todos los metodos CRUD de JpaRepository
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{

}
