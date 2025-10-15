package controller;

import entity.Solicitante;
import entity.Solicitud;
import service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SolicitanteService;

import java.util.List;

@RestController
@RequestMapping("/solicitantes")
public class SolicitanteController {

    private final SolicitanteService solicitanteService;

    @Autowired
    public SolicitanteController(SolicitanteService solicitanteService) {
        this.solicitanteService = solicitanteService;
    }

    // ✅ NUEVO ENDPOINT: GET /solicitantes/nombres-completos
    @GetMapping("/nombres-completos")
    public ResponseEntity<List<String>> obtenerNombresCompletos() {
        List<String> nombres = solicitanteService.obtenerNombresCompletos();
        return ResponseEntity.ok(nombres);
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
    
}
