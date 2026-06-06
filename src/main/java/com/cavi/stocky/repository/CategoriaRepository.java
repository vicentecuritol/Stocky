package com.cavi.stocky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavi.stocky.model.Categoria;

import java.util.Optional;

// accede a la tabla categoria en la base de datos
// JpaRepository nos da gratis: findAll, findById, save, deleteById, existsById y mas
// no necesitamos escribir SQL, JPA lo genera solo
@Repository // indica que esta interfaz es un componente de acceso a datos
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Categoria es el modelo, Long es el tipo del id

    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
