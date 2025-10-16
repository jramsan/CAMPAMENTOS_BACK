// SolicitudRepository.java
package repository;

import entity.Solicitud;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    // Devuelve true si ya existe una solicitud con ese solicitante y esa convocatoria
    boolean existsBySolicitante_IdAndConvocatoria_Id(Integer solicitanteId, Integer convocatoriaId);

    // ✅ NUEVO: devuelve los IDs de las convocatorias con más de X solicitudes
    @Query("SELECT s.convocatoria.id FROM Solicitud s GROUP BY s.convocatoria.id HAVING COUNT(s) > :min")
    List<Integer> findConvocatoriaIdsConMasDeSolicitudes(@Param("min") long min);

	List<Solicitud> findAll();

	void deleteAll();

	long count();

	Solicitud save(Solicitud any);
}
