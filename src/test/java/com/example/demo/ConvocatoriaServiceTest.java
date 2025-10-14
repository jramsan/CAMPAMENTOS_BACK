package com.example.demo;

import entity.Convocatoria;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ConvocatoriaRepository;
import service.ConvocatoriaService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConvocatoriaServiceTest {

    @Mock
    private ConvocatoriaRepository repo;

    @InjectMocks
    private ConvocatoriaService service;

    // ✅ Test OK: listar convocatorias
    @Test
    void testListarTodos_ok() {
        Convocatoria c1 = new Convocatoria();
        c1.setId(1);
        c1.setNombreConvocatoria("Campamento Primavera");

        when(repo.findAll()).thenReturn(List.of(c1));

        List<Convocatoria> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Campamento Primavera", resultado.get(0).getNombreConvocatoria());
    }

    // ❌ Test KO: no hay convocatorias
    @Test
    void testListarTodos_vacio_lanzaExcepcion() {
        when(repo.findAll()).thenReturn(List.of());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            service.listarTodos();
        });

        assertEquals("No existen convocatorias.", ex.getMessage());
    }

    // ✅ Test OK: crear correctamente
    @Test
    void testCrear_ok() {
        Convocatoria nueva = new Convocatoria();
        nueva.setNombreConvocatoria("Nueva Convocatoria");
        nueva.setFechaInicio(LocalDate.of(2025, 1, 1));
        nueva.setFechaFin(LocalDate.of(2025, 12, 31));

        when(repo.existsByNombreConvocatoria("Nueva Convocatoria")).thenReturn(false);
        when(repo.save(any(Convocatoria.class))).thenReturn(nueva);

        Convocatoria creada = service.crear(nueva);

        assertNotNull(creada);
        assertEquals("Nueva Convocatoria", creada.getNombreConvocatoria());
    }

    // ❌ Test KO: nombre ya existe
    @Test
    void testCrear_duplicado_lanzaAlreadyExistsException() {
        Convocatoria duplicada = new Convocatoria();
        duplicada.setNombreConvocatoria("Repetida");
        duplicada.setFechaInicio(LocalDate.of(2025, 6, 1));
        duplicada.setFechaFin(LocalDate.of(2025, 6, 30));

        when(repo.existsByNombreConvocatoria("Repetida")).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> {
            service.crear(duplicada);
        });

        assertEquals("Ya existe la convocatoria 'Repetida'", ex.getMessage());
    }

    // ✅ Test OK: borrar todos
    @Test
    void testBorrarTodos_ok() {
        when(repo.count()).thenReturn(2L);

        int borrados = service.borrarTodos();

        verify(repo).deleteAll();
        assertEquals(2, borrados);
    }

    // ❌ Test KO: nombre vacío
    @Test
    void testCrear_nombreVacio_lanzaIllegalArgumentException() {
        Convocatoria vacia = new Convocatoria();
        vacia.setNombreConvocatoria(" "); // solo espacio
        vacia.setFechaInicio(LocalDate.of(2025, 1, 1));
        vacia.setFechaFin(LocalDate.of(2025, 12, 31));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.crear(vacia);
        });

        assertEquals("El nombre de la convocatoria es obligatorio.", ex.getMessage());
    }

    // ❌ Test KO: fechas nulas
    @Test
    void testCrear_fechasNulas_lanzaIllegalArgumentException() {
        Convocatoria sinFechas = new Convocatoria();
        sinFechas.setNombreConvocatoria("Sin Fechas");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.crear(sinFechas);
        });

        assertEquals("Las fechas inicio y fin son obligatorias.", ex.getMessage());
    }

    // ❌ Test KO: fecha de inicio después de fecha fin
    @Test
    void testCrear_fechaInicioDespuesDeFin_lanzaExcepcion() {
        Convocatoria malFechada = new Convocatoria();
        malFechada.setNombreConvocatoria("Fechas Mal Puestas");
        malFechada.setFechaInicio(LocalDate.of(2025, 12, 31));
        malFechada.setFechaFin(LocalDate.of(2025, 1, 1));

        // Solo si añadiste esta validación en el service
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.crear(malFechada);
        });

        assertEquals("La fecha de inicio no puede ser posterior a la fecha de fin.", ex.getMessage());
    }
}


