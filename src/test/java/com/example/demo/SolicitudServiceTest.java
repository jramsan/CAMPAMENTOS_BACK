package com.example.demo;

import entity.Solicitud;
import entity.Solicitante;
import entity.Convocatoria;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import repository.SolicitudRepository;
import service.SolicitudService;
import repository.SolicitanteRepository;
import repository.ConvocatoriaRepository;
import DTO.GetSolicitudesByDuracionDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepo;

    @Mock
    private SolicitanteRepository solicitanteRepo;

    @Mock
    private ConvocatoriaRepository convocatoriaRepo;

    @InjectMocks
    private SolicitudService solicitudService;
    private Solicitante solicitante;
    private Convocatoria convocatoria;
    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitante = new Solicitante();
        solicitante.setId(1);
        solicitante.setNombre("Juan");
        solicitante.setApellido1("Pérez");
        solicitante.setApellido2("López");

        convocatoria = new Convocatoria();
        convocatoria.setId(10);
        convocatoria.setFechaInicio(LocalDate.of(2024, 1, 1));
        convocatoria.setFechaFin(LocalDate.of(2024, 1, 10)); // 9 días

        solicitud = new Solicitud();
        solicitud.setId(100);
        solicitud.setFechaSolicitud(LocalDate.of(2024, 1, 5));
        solicitud.setSolicitante(solicitante);
        solicitud.setConvocatoria(convocatoria);
        solicitud.setAdjudicada(false);
    }

    // ====== TEST: listarTodos ======
    @Test
    void listarTodos_ReturnsList_WhenSolicitudesExist() {
        List<Solicitud> solicitudes = Arrays.asList(solicitud);
        when(solicitudRepo.findAll()).thenReturn(solicitudes);

        List<Solicitud> result = solicitudService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(solicitud, result.get(0));
        verify(solicitudRepo).findAll();
    }

    @Test
    void listarTodos_ThrowsNotFoundException_WhenNoSolicitudes() {
        when(solicitudRepo.findAll()).thenReturn(new ArrayList<>());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            solicitudService.listarTodos();
        });

        assertEquals("No existen solicitudes.", exception.getMessage());
    }

    // ====== TEST: crear ======
    @Test
    void crear_ThrowsIllegalArgumentException_WhenSolicitanteIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.crear(null, 10, "2024-01-01", false);
        });
        assertEquals("solicitanteId es obligatorio.", ex.getMessage());
    }

    @Test
    void crear_ThrowsIllegalArgumentException_WhenFechaSolicitudIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.crear(1, 10, "", false);
        });
        assertEquals("fechaSolicitud es obligatoria (formato yyyy-MM-dd).", ex.getMessage());
    }

    @Test
    void crear_ThrowsNotFoundException_WhenSolicitanteNotFound() {
        when(solicitanteRepo.findById(1)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            solicitudService.crear(1, 10, "2024-01-01", false);
        });
        assertEquals("No existe el solicitante con id 1", ex.getMessage());
    }

    @Test
    void crear_ThrowsAlreadyExistsException_WhenDuplicate() {
        when(solicitanteRepo.findById(1)).thenReturn(Optional.of(solicitante));
        when(convocatoriaRepo.findById(10)).thenReturn(Optional.of(convocatoria));
        when(solicitudRepo.existsBySolicitante_IdAndConvocatoria_Id(1, 10)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> {
            solicitudService.crear(1, 10, "2024-01-01", false);
        });
        assertTrue(ex.getMessage().contains("Ya existe una solicitud"));
    }

    @Test
    void crear_ReturnsSolicitud_WhenValidInput() {
        when(solicitanteRepo.findById(1)).thenReturn(Optional.of(solicitante));
        when(convocatoriaRepo.findById(10)).thenReturn(Optional.of(convocatoria));
        when(solicitudRepo.existsBySolicitante_IdAndConvocatoria_Id(1, 10)).thenReturn(false);
        when(solicitudRepo.save(any(Solicitud.class))).thenReturn(solicitud);

        Solicitud result = solicitudService.crear(1, 10, "2024-01-05", false);

        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 1, 5), result.getFechaSolicitud());
        assertEquals(solicitante, result.getSolicitante());
        assertEquals(convocatoria, result.getConvocatoria());
        assertFalse(result.getAdjudicada());
        verify(solicitudRepo).save(any(Solicitud.class));
    }

    // ====== TEST: getSolcitudesByDuracion ======
    @Test
    void getSolcitudesByDuracion_ReturnsCorrectDTOList() {
        // Simular que hay una solicitud
        when(convocatoriaRepo.findById(10)).thenReturn(Optional.of(convocatoria));
        when(solicitanteRepo.findById(1)).thenReturn(Optional.of(solicitante));
        when(solicitudRepo.findAll()).thenReturn(Arrays.asList(solicitud));

        List<GetSolicitudesByDuracionDTO> result = solicitudService.getSolcitudesByDuracion();

        assertNotNull(result);
        assertEquals(1, result.size());
        GetSolicitudesByDuracionDTO dto = result.get(0);
        assertEquals(100, dto.idSolicitud);
        assertEquals("Juan Pérez López", dto.nombreUsuario);
        assertEquals(LocalDate.of(2024, 1, 5), dto.fechaSolicitud);
        assertEquals(9, dto.duracionEnDias); // 10 - 1 = 9 días
    }

    @Test
    void getSolcitudesByDuracion_ReturnsEmptyList_WhenNoSolicitudes() {
        when(solicitudRepo.findAll()).thenReturn(new ArrayList<>());

        List<GetSolicitudesByDuracionDTO> result = solicitudService.getSolcitudesByDuracion();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ====== TEST: borrarTodos ======
    @Test
    void borrarTodos_DeletesAllAndReturnsCount() {
        when(solicitudRepo.count()).thenReturn(3L);
        doNothing().when(solicitudRepo).deleteAll();

        int deleted = solicitudService.borrarTodos();

        assertEquals(3, deleted);
        verify(solicitudRepo).count();
        verify(solicitudRepo).deleteAll();
    }
}