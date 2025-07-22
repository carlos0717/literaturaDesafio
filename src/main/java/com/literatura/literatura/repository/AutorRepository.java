package com.literatura.literatura.repository;

import com.literatura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    //consulta para ver Listar nombres de autores registrados
    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> listarNombresAutoresRegistrados();

    // Consulta para listar autores vivos en un determinado año
    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros WHERE (a.fechaNacimiento IS NULL OR a.fechaNacimiento <= :anio) AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :anio)")
    List<Autor> listarAutoresVivosPorAnio(@Param("anio") Integer anio);
}
