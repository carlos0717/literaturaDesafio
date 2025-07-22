package com.literatura.literatura.principal;

import com.literatura.literatura.model.Datos;
import com.literatura.literatura.model.DatosLibros;
import com.literatura.literatura.model.DatosAutor;
import com.literatura.literatura.model.Libro;
import com.literatura.literatura.model.Autor;
import com.literatura.literatura.repository.LibroRepository;
import com.literatura.literatura.repository.AutorRepository;
import com.literatura.literatura.service.ConsumoAPI;
import com.literatura.literatura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Scanner sc = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private Optional<Libro> libroBuscado;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }
    public void muestraElMenu() {
        System.out.println("Bienvenido al sistema de literatura");
        var opcion = -1;
        while (opcion != 0) {
            //mostrar menu con opciones que sera gestionado con swich

            var menu = """
                    ********************************
                    Elija la opcion a trasves de su numero:
                    1. Buscar libro por titulo
                    2. listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idioma

                    0. Salir
                    """;
            System.out.println(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    buscarLibroDB();
                    break;
                case 3:
                    listarAutoresDB();
                    break;
                case 4:
                    System.out.println("Ingrese el anio");
                    int anio = sc.nextInt();
                    sc.nextLine();
                    listarAutoresVivosPorAnioDB(anio);
                    break;
                case 5:
                    System.out.println("Ingrese el idioma");
                    System.out.println("es - espanol");
                    System.out.println("en - ingles");
                    System.out.println("fr - frances");
                    System.out.println("pt - português");
                    String idioma = sc.next();
                    sc.nextLine();
                    listarLibrosPorIdiomaDB(idioma);
                    break;
                case 0:
                    System.exit(0);
                    break;
            }
        }
    }
    private DatosLibros getDatosLibro() {
        System.out.println("Ingrese el titulo del libro");
        var titulo = sc.nextLine();
        var json = consumoAPI.ObtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));

        // Parse the API response to get the list of books
        Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

        // Check if there are any results
        if (datos == null || datos.resultados() == null || datos.resultados().isEmpty()) {
            System.out.println("No se encontraron libros con ese título");
            return null;
        }

        // Return the first book in the list
        return datos.resultados().get(0);
    }

    private void buscarLibroWeb(){
        DatosLibros datosLibro = getDatosLibro();

        if (datosLibro == null) {
            System.out.println("No se encontró el libro");
            return;
        }

        // Check if a book with the same title already exists in the database
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("El libro ya se encuentra en la base de datos");
            return;
        }

        // Create and save the Autor first if it exists
        Autor autor = null;
        if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
            DatosAutor datosAutor = datosLibro.autores().get(0);
            // Check if an author with the same name already exists
            List<Autor> autores = autorRepository.findAll();
            boolean autorExiste = false;

            for (Autor a : autores) {
                if (a.getNombre() != null && a.getNombre().equals(datosAutor.nombre())) {
                    autor = a;
                    autorExiste = true;
                    break;
                }
            }

            if (!autorExiste) {
                autor = new Autor();
                autor.setNombre(datosAutor.nombre());
                autor.setFechaNacimiento(datosAutor.fechaNacimiento());
                autor.setFechaMuerte(datosAutor.fechaMuerte());
                autorRepository.save(autor);
            }
        }

        // Create the Libro with the saved Autor
        Libro libro = new Libro();
        libro.setTitulo(datosLibro.titulo());
        libro.setAutor(autor);

        // Set the language if it exists
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            libro.setIdioma(datosLibro.idiomas().get(0));
        }

        libro.setNumDescargas(datosLibro.numDescargas());

        // Save the Libro
        libroRepository.save(libro);

        System.out.println("Libro guardado en la base de datos:");
        System.out.println("Título: " + libro.getTitulo());
        if (autor != null) {
            System.out.println("Autor: " + autor.getNombre());
        }
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Número de descargas: " + libro.getNumDescargas());
    }

    private void buscarLibroDB(){
        libros = libroRepository.findAll();
        System.out.println("Libros registrados en la base de datos:");
        libros.forEach(l -> {
            System.out.println("****************LIBRO****************");
            System.out.println("Título: " + l.getTitulo());
            System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Sin autor"));
            System.out.println("Idioma: " + l.getIdioma());
            System.out.println("Numero de Descargas: " + l.getNumDescargas());
            System.out.println("****************************************");
            System.out.println();
        });
    }

    private void listarAutoresDB(){
        List<Autor> autores = autorRepository.listarNombresAutoresRegistrados();
        System.out.println("Autores registrados en la base de datos:");
        autores.forEach(a ->{
            System.out.println("****************AUTOR****************");
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Nacimiento: " + (a.getFechaNacimiento() != null ? a.getFechaNacimiento() : "Desconocido"));
            System.out.println("Muerte: " + (a.getFechaMuerte() != null ? a.getFechaMuerte() : "Desconocido"));
            //mostrar los titulos de los libros del autor
            List<Libro> libros = a.getLibros();
            if (libros != null && !libros.isEmpty()) {
                libros.forEach(l -> {
                    System.out.println("Libro: [" + l.getTitulo()+"]");
                });
            } else {
                System.out.println("No tiene libros registrados");
            }

            System.out.println("****************************************");
            System.out.println();
        });
    }

    private void listarAutoresVivosPorAnioDB(int anio){
        List<Autor> autores = autorRepository.listarAutoresVivosPorAnio(anio);
        System.out.println("Autores vivos en el año " + anio + ":");
        autores.forEach(a -> {
            System.out.println("****************AUTOR****************");
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Nacimiento: " + (a.getFechaNacimiento() != null ? a.getFechaNacimiento() : "Desconocido"));
            System.out.println("Muerte: " + (a.getFechaMuerte() != null ? a.getFechaMuerte() : "Desconocido"));
            //mostrar los titulos de los libros del autor
            List<Libro> libros = a.getLibros();
            if (libros != null && !libros.isEmpty()) {
                libros.forEach(l -> {
                    System.out.println("Libro(s): [" + l.getTitulo()+"]");
                });
            } else {
                System.out.println("No tiene libros registrados");
            }
            System.out.println("****************************************");
            System.out.println();
        });
    }

    private void listarLibrosPorIdiomaDB(String idioma){
        // Verificar que el idioma tenga exactamente 2 letras
        if (idioma.length() != 2) {
            System.out.println("No está ingresando un valor válido de idioma (ejm: es, en ...)");
            return;
        }

        libros = libroRepository.findByIdiomaIgnoreCase(idioma);

        // Verificar si se encontraron libros en ese idioma
        if (libros.isEmpty()) {
            System.out.println("No existe libros en ese idioma en la base de datos");
            return;
        }

        System.out.println("Libros en idioma " + idioma + ":");
        libros.forEach(l -> {
            System.out.println("****************LIBRO****************");
            System.out.println("Título: " + l.getTitulo());
            System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Sin autor"));
            System.out.println("Idioma: " + idioma);
            System.out.println("Descargas: " + l.getNumDescargas());
            System.out.println("****************************************");
        });
    }
}
