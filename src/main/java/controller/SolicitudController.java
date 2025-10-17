// SolicitudController.java
package controller;

import service.SolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import DTO.GetSolicitudesByDuracionDTO;

import java.net.URI;
import java.util.List;
import java.util.Map;

import entity.Solicitud;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @GetMapping
    public List<Solicitud> getAll() {
        return service.listarTodos();
    }

    /**
     * Crea una solicitud.
     * Body esperado (JSON):
     * {
     *   "solicitanteId": 1,
     *   "convocatoriaId": 1,
     *   "fechaSolicitud": "2025-05-15",
     *   "adjudicada": true
     * }
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        // Extracción y validaciones "suaves"
        Integer solicitanteId = getInt(body.get("solicitanteId"), "solicitanteId");
        Integer convocatoriaId = getInt(body.get("convocatoriaId"), "convocatoriaId");
        String fechaSolicitud = getString(body.get("fechaSolicitud"), "fechaSolicitud");
        Boolean adjudicada = body.get("adjudicada") instanceof Boolean ? (Boolean) body.get("adjudicada") : null;

        Solicitud creada = service.crear(solicitanteId, convocatoriaId, fechaSolicitud, adjudicada);
        return ResponseEntity
                .created(URI.create("/api/solicitudes/" + creada.getId()))
                .body(creada);
    }

    @GetMapping("/duracion")
    public List<GetSolicitudesByDuracionDTO> getSolcitudesByDuracion() {
        return service.getSolcitudesByDuracion();
    }

    // ✅ NUEVO ENDPOINT: devuelve las 3 solicitudes cuyas convocatorias tienen mayor duración
    @GetMapping("/top3duracion")
    public ResponseEntity<List<Solicitud>> getTop3PorDuracionConvocatoria() {
        List<Solicitud> result = service.getTop3SolicitudesPorDuracionConvocatoria();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> borrarTodos() {
        int borrados = service.borrarTodos();
        return ResponseEntity.ok(Map.of(
                "message", "Solicitudes borradas",
                "count", borrados
        ));
    }

    // ---- Helpers de parseo con mensajes claros (caen en 400 Bad Request por nuestro Handler) ----
    private Integer getInt(Object val, String field) {
        if (val == null) throw new IllegalArgumentException(field + " es obligatorio.");
        if (val instanceof Integer i) return i;
        if (val instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(val.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(field + " debe ser numérico.");
        }
    }

    private String getString(Object val, String field) {
        if (val == null) throw new IllegalArgumentException(field + " es obligatorio.");
        String s = val.toString().trim();
        if (s.isEmpty()) throw new IllegalArgumentException(field + " es obligatorio.");
        return s;
    }

}
