package controller;

import entity.Solicitante;
import entity.Solicitud;
import exception.NotFoundException;
import service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import service.SolicitanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitantes")
public class SolicitanteController {

    private final SolicitanteService service;

    public SolicitanteController(SolicitanteService service) {
        this.service = service;
    }

    // 🔍 Nuevo: buscar por apellido1
    @GetMapping(params = "apellido1")
    public List<Solicitante> getByApellido1(@RequestParam String apellido1) {
        return service.buscarPorApellido1(apellido1);
    }

    @GetMapping
    public List<Solicitante> getAll() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Solicitante> crear(@RequestBody Solicitante s) {
        Solicitante creado = service.crear(s);
        return ResponseEntity
                .created(URI.create("/api/solicitantes/" + creado.getId()))
                .body(creado);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> borrarTodos() {
        int borrados = service.borrarTodos();
        return ResponseEntity.ok(Map.of(
                "message", "Solicitantes borrados",
                "count", borrados
        ));
    }
 // ✅ Nuevo endpoint: obtener solicitantes mayores de edad
    @GetMapping("/mayores-de-edad")
    public ResponseEntity<?> getMayoresDeEdad() {
        try {
            return ResponseEntity.ok(service.obtenerMayoresDeEdad());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "No existen solicitantes mayores de edad", e.getMessage()
            ));
        }
    }

    
    
}


