// ConvocatoriaService.java
package service;

import entity.Convocatoria;
import entity.Solicitud;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import repository.ConvocatoriaRepository;
import repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConvocatoriaService {
    private final ConvocatoriaRepository repo;
    private final SolicitudRepository solicitudRepo;

    public ConvocatoriaService(ConvocatoriaRepository repo, SolicitudRepository solicitudRepo) {
        this.repo = repo;
        this.solicitudRepo = solicitudRepo;
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
        if (c.getFechaInicio().isAfter(c.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
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

    // ✅ NUEVO: obtener convocatorias con más de X solicitudes
    public List<Convocatoria> obtenerConMasDeSolicitudes(long minSolicitudes) {
        List<Integer> ids = solicitudRepo.findConvocatoriaIdsConMasDeSolicitudes(minSolicitudes);
        if (ids.isEmpty()) {
            throw new NotFoundException("No hay convocatorias con más de " + minSolicitudes + " solicitudes.");
        }
        return repo.findAllById(ids);
    }
    // Obtener convocatorias futuras
    public List<Convocatoria> listarFuturas() {
        LocalDate hoy = LocalDate.now();
        List<Convocatoria> futuras = repo.findByFechaInicioAfter(hoy);
        if (futuras.isEmpty()) {
            throw new NotFoundException("No hay convocatorias futuras.");
        }
        return futuras;
    }
}
