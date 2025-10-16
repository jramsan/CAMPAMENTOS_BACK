// SolicitanteRepository.java
package repository;

import entity.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Integer> {
    boolean existsByDni(String dni);

    // 🔍 Este método busca por el campo "apellido1"
    List<Solicitante> findByApellido1(String apellido1);

	Optional<Solicitante> findById(Integer id);
}
