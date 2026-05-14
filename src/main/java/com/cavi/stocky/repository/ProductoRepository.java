package com.cavi.stocky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Producto;

// accede a la tabla producto en la base de datos
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM producto p WHERE p.stockActual <= p.stockMinimo")
    List<Producto> findByStockActualLessThanEqualStockMinimo();

}
