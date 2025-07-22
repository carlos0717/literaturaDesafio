package com.literatura.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String idioma;
    private Double numDescargas;
    //Relacion un libro tiene un autor

    public Libro() {
    }

    public Libro(DatosLibros libro) {
        this.titulo = libro.titulo();
        // Assuming the first author in the list is the main author
        if (libro.autores() != null && !libro.autores().isEmpty()) {
            Autor autor = new Autor(
                libro.autores().get(0).nombre(),
                libro.autores().get(0).fechaNacimiento(),
                libro.autores().get(0).fechaMuerte()
            );
            this.autor = autor;
        }
        // Assuming the first language in the list is the main language
        if (libro.idiomas() != null && !libro.idiomas().isEmpty()) {
            this.idioma = libro.idiomas().get(0);
        }
        this.numDescargas = libro.numDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumDescargas() {
        return numDescargas;
    }

    public void setNumDescargas(Double numDescargas) {
        this.numDescargas = numDescargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + (autor != null ? autor.getNombre() : "Sin autor") +
                ", idioma='" + idioma + '\'' +
                ", numDescargas=" + numDescargas +
                '}';
    }
}
