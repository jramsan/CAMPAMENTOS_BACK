// ConvocatoriaService.java
package service;

import entity.Convocatoria;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import repository.ConvocatoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvocatoriaService {
    private final ConvocatoriaRepository repo;

    public ConvocatoriaService(ConvocatoriaRepository repo) {
        this.repo = repo;
    }

    public List<Convocatoria> listarTodos() {
        List<Convocatoria> list = repo.findAll();
        if (list.isEmpty()) throw new NotFoundException("No existen convocatorias.");
        return list;
    }

    public Convocatoria crear(Convocatoria c) {
        if (c.getNombreConvocatoria() == null || c.getNombreConvocatoria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la convocatoria es obligatorio.");
        }
        if (c.getFechaInicio() == null || c.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas inicio y fin son obligatorias.");
        }
        if (repo.existsByNombreConvocatoria(c.getNombreConvocatoria())) {
            throw new AlreadyExistsException("Ya existe la convocatoria '" + c.getNombreConvocatoria() + "'");
        }
        return repo.save(c);
    }

    public int borrarTodos() {
        int count = (int) repo.count();
        repo.deleteAll();
        return count;
    }
}
