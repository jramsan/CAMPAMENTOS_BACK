package controller;

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

}
