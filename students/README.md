# API REST de Estudiantes
## Arquitectura por Capas
El proyecto está estructurado de la siguiente manera:
1. **Controllers:** Manejan las peticiones HTTP (`StudentController`).
2. **Services:** Contienen la lógica de negocio (`StudentService`).
3. **Repositories:** Gestionan el acceso a la base de datos H2 (`StudentRepository`).
4. **Entities & DTOs:** Representan el modelo de base de datos y los objetos de transferencia de datos.

## Endpoints Disponibles

### 1. Crear un estudiante
Método: `POST`

Ruta: `/students`
### 2. Listar todos los estudiantes
   Método: GET

Ruta: /students

Lista en formato JSON con los estudiantes registrados.

### Pruebas
Se incluye en la raíz del repositorio un archivo .json exportado desde Postman con la colección de pruebas listas para ejecutarse.