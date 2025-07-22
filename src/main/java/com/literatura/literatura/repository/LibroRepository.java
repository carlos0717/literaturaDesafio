package com.literatura.literatura.repository;

import com.literatura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    // Consulta para listar libros por idioma
    @Query("SELECT l FROM Libro l WHERE LOWER(l.idioma) = LOWER(:idioma)")
    List<Libro> findByIdiomaIgnoreCase(@Param("idioma") String idioma);
}
