package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository <Movimiento, Long>{

}
