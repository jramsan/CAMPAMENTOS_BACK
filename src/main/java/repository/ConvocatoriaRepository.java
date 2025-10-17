// ConvocatoriaRepository.java
package repository;

import entity.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer> {

    boolean existsByNombreConvocatoria(String nombreConvocatoria);

    // 🔹 Método adicional personalizado (este sí es válido)
    List<Convocatoria> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy1, LocalDate hoy2);
}
