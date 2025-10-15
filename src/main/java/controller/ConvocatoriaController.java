// ConvocatoriaController.java
package controller;

import entity.Convocatoria;
import entity.Solicitud;
import service.ConvocatoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/convocatorias")
public class ConvocatoriaController {

    private final ConvocatoriaService service;

    public ConvocatoriaController(ConvocatoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Convocatoria> getAll() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Convocatoria> crear(@RequestBody Convocatoria c) {
        Convocatoria creada = service.crear(c);
        return ResponseEntity
                .created(URI.create("/api/convocatorias/" + creada.getId()))
                .body(creada);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> borrarTodos() {
        int borrados = service.borrarTodos();
        return ResponseEntity.ok(Map.of(
                "message", "Convocatorias borradas",
                "count", borrados
        ));
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<Convocatoria>> getSolicitudesActivas() {
        List<Convocatoria> activas = service.obtenerSolicitudesActivasHoy();
        return ResponseEntity.ok(activas);
    }
}
