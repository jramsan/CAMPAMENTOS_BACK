package service;

import entity.Campamento;
import exception.NotFoundException;
import repository.CampamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampamentoService {

    private final CampamentoRepository repo;

    public CampamentoService(CampamentoRepository repo) {
        this.repo = repo;
    }

    // 🔹 Crear
    public Campamento crear(Campamento c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del campamento es obligatorio.");
        }
        if (c.getDestino() == null) {
            throw new IllegalArgumentException("El destino del campamento es obligatorio.");
        }
        return repo.save(c);
    }

    // 🔹 Leer todos
    public List<Campamento> listarTodos() {
        List<Campamento> list = repo.findAll();
        if (list.isEmpty()) throw new NotFoundException("No existen campamentos registrados.");
        return list;
    }

    // 🔹 Leer por ID
    public Campamento obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Campamento con ID " + id + " no encontrado."));
    }

    // 🔹 Actualizar
    public Campamento actualizar(Integer id, Campamento c) {
        Campamento existente = obtenerPorId(id);
        existente.setNombre(c.getNombre());
        existente.setDestino(c.getDestino());
        return repo.save(existente);
    }

    // 🔹 Borrar
    public void borrar(Integer id) {
        Campamento existente = obtenerPorId(id);
        repo.delete(existente);
    }
}
