// SolicitudService.java
package service;

import entity.Solicitud;
import entity.Solicitante;
import entity.Convocatoria;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import repository.SolicitudRepository;
import repository.SolicitanteRepository;
import repository.ConvocatoriaRepository;
import org.springframework.stereotype.Service;

import DTO.GetSolicitudesByDuracionDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepo;
    private final SolicitanteRepository solicitanteRepo;
    private final ConvocatoriaRepository convocatoriaRepo;

    public SolicitudService(SolicitudRepository solicitudRepo,
                            SolicitanteRepository solicitanteRepo,
                            ConvocatoriaRepository convocatoriaRepo) {
        this.solicitudRepo = solicitudRepo;
        this.solicitanteRepo = solicitanteRepo;
        this.convocatoriaRepo = convocatoriaRepo;
    }

    public List<Solicitud> listarTodos() {
        List<Solicitud> list = solicitudRepo.findAll();
        if (list.isEmpty()) throw new NotFoundException("No existen solicitudes.");
        return list;
    }
    
    public List<GetSolicitudesByDuracionDTO> getSolcitudesByDuracion(){
    	
    	List<GetSolicitudesByDuracionDTO> listGetSolByDuracion = new ArrayList<>();
    	GetSolicitudesByDuracionDTO getSolByDuracion;
    	List<Solicitud> solicitudes = Optional.of(solicitudRepo.findAll()).orElseThrow(()-> new RuntimeException("No existen Solicitudes"));
    	
    	for(Solicitud  s : solicitudes) {
    		getSolByDuracion = new GetSolicitudesByDuracionDTO();
    		
    		getSolByDuracion.idSolicitud = s.getId();
    		getSolByDuracion.fechaSolicitud = s.getFechaSolicitud();
    		
    		Solicitante solicitante = new Solicitante(); 
    		solicitante = Optional.of(solicitanteRepo.findById(s.getSolicitante().getId()).get()).orElseThrow(()->new RuntimeException("No existe solicitante"));
    		
    		getSolByDuracion.nombreUsuario = solicitante.getNombre()     + " " + 
    		                                 solicitante.getApellido1()  + " " +
    		                                 solicitante.getApellido2();
    		
    		Convocatoria convocatoria = new Convocatoria();
    		convocatoria = Optional.of(convocatoriaRepo.findById(s.getConvocatoria().getId()).get()).orElseThrow(()->new RuntimeException("No existe convocatoria")) ;
    		getSolByDuracion.duracionEnDias = (int) ChronoUnit.DAYS.between(convocatoria.getFechaInicio(), convocatoria.getFechaFin());
     		
    		listGetSolByDuracion.add(getSolByDuracion); 		
    		
    	}
    	return listGetSolByDuracion;
    	
    }
    

    /**
     * Crea una solicitud validando:
     *  - Que existan el solicitante y la convocatoria
     *  - Que no exista ya una solicitud con el mismo (solicitante, convocatoria)
     *  - Que la fecha tenga formato ISO (yyyy-MM-dd)
     */
    public Solicitud crear(Integer solicitanteId,
                           Integer convocatoriaId,
                           String fechaSolicitud,
                           Boolean adjudicada) {

        if (solicitanteId == null) throw new IllegalArgumentException("solicitanteId es obligatorio.");
        if (convocatoriaId == null) throw new IllegalArgumentException("convocatoriaId es obligatorio.");
        if (fechaSolicitud == null || fechaSolicitud.isBlank())
            throw new IllegalArgumentException("fechaSolicitud es obligatoria (formato yyyy-MM-dd).");

        // Parseo de fecha (si falla, saltará IllegalArgumentException -> 400 por nuestro Handler)
        LocalDate fecha = LocalDate.parse(fechaSolicitud);

        // Cargar entidades (si no existen -> 404 NotFound)
        Solicitante solicitante = solicitanteRepo.findById(solicitanteId)
                .orElseThrow(() -> new NotFoundException("No existe el solicitante con id " + solicitanteId));

        Convocatoria convocatoria = convocatoriaRepo.findById(convocatoriaId)
                .orElseThrow(() -> new NotFoundException("No existe la convocatoria con id " + convocatoriaId));

        // Regla de unicidad por (solicitante, convocatoria)
        boolean yaExiste = solicitudRepo
                .existsBySolicitante_IdAndConvocatoria_Id(solicitanteId, convocatoriaId);
        if (yaExiste) {
            throw new AlreadyExistsException(
                    "Ya existe una solicitud para el solicitante " + solicitanteId +
                            " en la convocatoria " + convocatoriaId
            );
        }

        // Crear y guardar
        Solicitud s = new Solicitud();
        s.setFechaSolicitud(fecha);
        s.setAdjudicada(adjudicada != null ? adjudicada : Boolean.FALSE);
        s.setSolicitante(solicitante);
        s.setConvocatoria(convocatoria);
        return solicitudRepo.save(s);
    }

    public int borrarTodos() {
        int count = (int) solicitudRepo.count();
        solicitudRepo.deleteAll();
        return count;
    }
}
