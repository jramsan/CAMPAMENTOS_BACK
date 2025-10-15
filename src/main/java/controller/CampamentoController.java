package controller;

import entity.Campamento;
import service.CampamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campamentos")
public class CampamentoController {

    private final CampamentoService service;

    public CampamentoController(CampamentoService service) {
        this.service = service;
    }

    // 🔹 Crear
    @PostMapping
    public ResponseEntity<Campamento> crear(@RequestBody Campamento c) {
        Campamento creado = service.crear(c);
        return ResponseEntity
                .created(URI.create("/api/campamentos/" + creado.getId()))
                .body(creado);
    }

    // 🔹 Leer todos
    @GetMapping
    public List<Campamento> listarTodos() {
        return service.listarTodos();
    }

    // 🔹 Leer por ID
    @GetMapping("/{id}")
    public Campamento obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    // 🔹 Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Campamento> actualizar(@PathVariable Integer id, @RequestBody Campamento c) {
        Campamento actualizado = service.actualizar(id, c);
        return ResponseEntity.ok(actualizado);
    }

    // 🔹 Borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> borrar(@PathVariable Integer id) {
        service.borrar(id);
        return ResponseEntity.ok(Map.of("message", "Campamento con ID " + id + " eliminado correctamente"));
    }
}
