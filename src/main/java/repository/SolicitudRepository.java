// SolicitudRepository.java
package repository;

import entity.Solicitud;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    // Devuelve true si ya existe una solicitud con ese solicitante y esa convocatoria
    boolean existsBySolicitante_IdAndConvocatoria_Id(Integer solicitanteId, Integer convocatoriaId);
}
