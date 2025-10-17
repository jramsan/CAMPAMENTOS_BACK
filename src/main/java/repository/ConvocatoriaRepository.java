// ConvocatoriaRepository.java
package repository;

import entity.Convocatoria;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer> {

    boolean existsByNombreConvocatoria(String nombreConvocatoria);
    List<Convocatoria> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy1, LocalDate hoy2);
	  Optional<Convocatoria> findById(Integer id);
  	List<Convocatoria> findAll();
  	List<Convocatoria> findByFechaInicioAfter(LocalDate fecha);
    // Devuelve las convocatorias activas en un rango de fechas
    List<Convocatoria> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            LocalDate hoy1, LocalDate hoy2);

    // Estos dos métodos ya están definidos en JpaRepository,
    // por lo tanto no hace falta redefinirlos:
    // Optional<Convocatoria> findById(Integer id);
    // List<Convocatoria> findAll();
    // List<Convocatoria> findAllById(List<Integer> ids);
}
