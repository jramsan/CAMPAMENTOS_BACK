// ConvocatoriaRepository.java
package repository;

import entity.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer> {
    boolean existsByNombreConvocatoria(String nombreConvocatoria);
}
