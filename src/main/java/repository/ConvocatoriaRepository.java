// ConvocatoriaRepository.java
package repository;

import entity.Convocatoria;
import entity.Solicitud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer> {
    boolean existsByNombreConvocatoria(String nombreConvocatoria);
    List<Convocatoria> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy1, LocalDate hoy2);
	Optional<Convocatoria> findById(Integer id);
	List<Convocatoria> findAll();
	//List<Convocatoria> findAllById(List<Integer> ids);
	List<Convocatoria> findByFechaInicioAfter(LocalDate fecha);

}
