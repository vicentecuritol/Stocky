package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Producto;

// accede a la tabla producto en la base de datos
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
