# Literatura - Sistema de Gestión de Literatura

## Descripción
Literatura es una aplicación de consola desarrollada en Java con Spring Boot que permite gestionar información sobre libros y autores. La aplicación se conecta a la API externa de Gutendex para obtener datos de libros y los almacena en una base de datos local PostgreSQL.

## Tecnologías Utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Jackson (para procesamiento de JSON)
- Maven (para gestión de dependencias)

## Estructura del Proyecto
```
literatura/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── literatura/
│   │   │           └── literatura/
│   │   │               ├── model/
│   │   │               │   ├── Autor.java
│   │   │               │   ├── Datos.java
│   │   │               │   ├── DatosAutor.java
│   │   │               │   ├── DatosLibros.java
│   │   │               │   └── Libro.java
│   │   │               ├── principal/
│   │   │               │   └── Principal.java
│   │   │               ├── repository/
│   │   │               │   ├── AutorRepository.java
│   │   │               │   └── LibroRepository.java
│   │   │               ├── service/
│   │   │               │   ├── ConsumoAPI.java
│   │   │               │   ├── ConvierteDatos.java
│   │   │               │   └── IConvierteDatos.java
│   │   │               └── LiteraturaApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── literatura/
│                   └── literatura/
│                       └── LiteraturaApplicationTests.java
└── pom.xml
```

## Funcionalidades
La aplicación ofrece las siguientes funcionalidades:

1. **Buscar libro por título**: Busca un libro en la API de Gutendex y lo guarda en la base de datos local.
2. **Listar libros registrados**: Muestra todos los libros almacenados en la base de datos.
3. **Listar autores registrados**: Muestra todos los autores almacenados en la base de datos junto con sus libros.
4. **Listar autores vivos en un determinado año**: Muestra los autores que estaban vivos en un año específico.
5. **Listar libros por idioma**: Muestra los libros filtrados por idioma (ej: es, en, fr, pt).

## Configuración y Ejecución

### Requisitos Previos
- Java 17 o superior
- PostgreSQL
- Maven

### Configuración de la Base de Datos
1. Crear una base de datos PostgreSQL llamada `literatura`
2. Configurar las credenciales en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
   spring.datasource.username=postgres
   spring.datasource.password=contraseña-postgres
   ```

### Ejecución
1. Clonar el repositorio
2. Navegar al directorio del proyecto
3. Ejecutar el comando: `mvn spring-boot:run`

## Modelo de Datos

### Libro
- **id**: Identificador único del libro
- **titulo**: Título del libro
- **autor**: Referencia al autor del libro
- **idioma**: Idioma del libro (código de 2 letras, ej: es, en)
- **numDescargas**: Número de descargas del libro

### Autor
- **id**: Identificador único del autor
- **nombre**: Nombre del autor
- **fechaNacimiento**: Año de nacimiento del autor
- **fechaMuerte**: Año de fallecimiento del autor
- **libros**: Lista de libros escritos por el autor

## API Externa
La aplicación se conecta a la API de Gutendex (https://gutendex.com/books/) para obtener información sobre libros y autores. Esta API proporciona acceso a la colección de libros del Proyecto Gutenberg.

## Uso
Al iniciar la aplicación, se muestra un menú con las opciones disponibles:

```
********************************
Elija la opcion a trasves de su numero:
1. Buscar libro por titulo
2. listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un determinado año
5. Listar libros por idioma

0. Salir
```

Seleccione la opción deseada ingresando el número correspondiente y siga las instrucciones en pantalla.
