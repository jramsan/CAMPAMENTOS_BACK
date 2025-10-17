// SolicitanteService.java
package service;

import entity.Solicitante;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import repository.SolicitanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class SolicitanteService {
    private final SolicitanteRepository repo;

    public SolicitanteService(SolicitanteRepository repo) {
        this.repo = repo;
    }

    public List<Solicitante> listarTodos() {
        List<Solicitante> list = repo.findAll();
        if (list.isEmpty()) throw new NotFoundException("No existen solicitantes.");
        return list;
    }

    public Solicitante crear(Solicitante s) {
        if (s.getDni() == null || s.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio.");
        }
        if (s.getNombre() == null || s.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (s.getApellido1() == null || s.getApellido1().isBlank()) {
            throw new IllegalArgumentException("El apellido1 es obligatorio.");
        }
        if (repo.existsByDni(s.getDni())) {
            throw new AlreadyExistsException("Ya existe un solicitante con DNI " + s.getDni());
        }
        return repo.save(s);
    }

    public int borrarTodos() {
        int count = (int) repo.count();
        repo.deleteAll();
        return count;
    }

    // 🔍 MÉTODO NUEVO: Buscar por apellido1
    public List<Solicitante> buscarPorApellido1(String apellido1) {
        List<Solicitante> list = repo.findByApellido1(apellido1);
        if (list.isEmpty()) {
            throw new NotFoundException("No se encontraron solicitantes con el apellido: " + apellido1);
        }
        return list;
    }
 // ✅ Nuevo método: obtener solicitantes mayores de edad
    public List<Solicitante> obtenerMayoresDeEdad() {
        List<Solicitante> todos = repo.findAll();
        if (todos.isEmpty()) throw new NotFoundException("No existen solicitantes.");

        LocalDate hoy = LocalDate.now();

        List<Solicitante> mayores = todos.stream()
                .filter(s -> s.getFechaNacimiento() != null)
                .filter(s -> Period.between(s.getFechaNacimiento(), hoy).getYears() >= 18)
                .toList();

        if (mayores.isEmpty()) {
            throw new NotFoundException("No hay solicitantes mayores de edad.");
        }

        return mayores;
    }

    
}
