package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Movimiento;

// accede a la tabla movimiento, guarda el historial de entradas y salidas
@Repository
public interface MovimientoRepository extends JpaRepository <Movimiento, Long>{

}
