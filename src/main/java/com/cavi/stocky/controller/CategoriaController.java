package com.cavi.stocky.controller;


import com.cavi.stocky.dto.CategoriaResponse;
import com.cavi.stocky.model.Categoria;
import com.cavi.stocky.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@AllArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    //Muestra todas las categorias registradas
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        List<Categoria> categorias = categoriaService.getCategorias();
        List<CategoriaResponse> respuestas = categorias.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);

    }

    //Extraela categoria por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.getCategoriaId(id);
        if (categoria != null) {
            return ResponseEntity.ok(convertirAResponse(categoria));
        }
        return ResponseEntity.notFound().build();
    }

    //Crea una nueva categoria
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.saveCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAResponse(nueva));
    }

    //actualiza la categoria
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria actualizada = categoriaService.updateCategoria(categoria);
        if (actualizada != null) {
            return ResponseEntity.ok(convertirAResponse(actualizada));
        }
        return ResponseEntity.notFound().build();
    }

    //Elimina la actegoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (categoriaService.existeCategoria(id)) {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    private CategoriaResponse convertirAResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

}
