package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cavi.stocky.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
