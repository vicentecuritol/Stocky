package com.cavi.stocky.controller;


import com.cavi.stocky.dto.CategoriaResponseDto;
import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;
// controller de categoria, recibe peticiones HTTP en /api/v1/categorias
@RestController // indica que esta clase maneja peticiones HTTP y devuelve JSON
@RequestMapping("/api/v1/categorias") // URL base para todos los endpoints de esta clase
@AllArgsConstructor// lombok genera el constructor inyectando el service
public class CategoriaController {
    private final CategoriaService categoriaService;

    // GET /api/v1/categorias - trae todas las categorias
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> obtenerTodas() {
        List<Categoria> categorias = categoriaService.getCategorias();
        List<CategoriaResponseDto> respuestas = categorias.stream()
                .map(this::convertirAResponse)// convierte cada Categoria a su DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas); // responde 200 OK con la lista

    }

    // GET /api/v1/categorias/{id} - busca una categoria por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> obtenerPorId(@PathVariable Long id) {
        // @PathVariable captura el {id} de la URL y lo convierte al parametro
        Categoria categoria = categoriaService.getCategoriaId(id);
        if (categoria != null) {
            return ResponseEntity.ok(convertirAResponse(categoria));
        }
        return ResponseEntity.notFound().build();// responde 404 si no existe
    }

    // POST /api/v1/categorias - crea una categoria nueva
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> crear(@Valid @RequestBody Categoria categoria) {
        // @Valid activa las validaciones del modelo antes de continuar
        // @RequestBody convierte el JSON del cliente en objeto Categoria
        Categoria nueva = categoriaService.saveCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nueva)); // responde 201 Created
    }

    // PUT /api/v1/categorias/{id} - actualiza una categoria existente
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        categoria.setId(id); // asignamos el id de la URL para que el service sepa cual actualizar
        Categoria actualizada = categoriaService.updateCategoria(categoria);
        if (actualizada != null) {
            return ResponseEntity.ok(convertirAResponse(actualizada));
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/v1/categorias/{id} - elimina una categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (categoriaService.existeCategoria(id)) {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.noContent().build(); // responde 204, eliminado sin contenido
        }
        return ResponseEntity.notFound().build();
    }


    // convierte el modelo Categoria al DTO para no exponer la entidad directo al cliente
    private CategoriaResponseDto convertirAResponse(Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

}
