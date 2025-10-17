package com.example.demo;

import entity.Convocatoria;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import repository.ConvocatoriaRepository;
import repository.SolicitudRepository;
import service.ConvocatoriaService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConvocatoriaServiceTest {

	private ConvocatoriaRepository convocatoriaRepository;
	private SolicitudRepository solicitudRepository;
	private ConvocatoriaService convocatoriaService;

	@BeforeEach
	void setUp() {
		convocatoriaRepository = mock(ConvocatoriaRepository.class);
		solicitudRepository = mock(SolicitudRepository.class);
		convocatoriaService = new ConvocatoriaService(convocatoriaRepository, solicitudRepository);
	}

	@Test
	void testListarTodosSuccess() {
		List<Convocatoria> mockList = List.of(new Convocatoria());
		when(convocatoriaRepository.findAll()).thenReturn(mockList);

		List<Convocatoria> result = convocatoriaService.listarTodos();

		assertEquals(1, result.size());
	}

	@Test
	void testListarTodosThrowsNotFound() {
		when(convocatoriaRepository.findAll()).thenReturn(Collections.emptyList());

		NotFoundException ex = assertThrows(NotFoundException.class, () -> convocatoriaService.listarTodos());
		assertEquals("No existen convocatorias.", ex.getMessage());
	}

	@Test
	void testCrearSuccess() {
		Convocatoria nueva = new Convocatoria();
		nueva.setNombreConvocatoria("Nueva");
		nueva.setFechaInicio(LocalDate.of(2025, 1, 1));
		nueva.setFechaFin(LocalDate.of(2025, 12, 31));

		when(convocatoriaRepository.existsByNombreConvocatoria("Nueva")).thenReturn(false);
		when(convocatoriaRepository.save(nueva)).thenReturn(nueva);

		Convocatoria result = convocatoriaService.crear(nueva);

		assertEquals("Nueva", result.getNombreConvocatoria());
	}

	@Test
	void testCrearThrowsIllegalArgument_blankName() {
		Convocatoria c = new Convocatoria();
		c.setNombreConvocatoria("  ");
		c.setFechaInicio(LocalDate.now());
		c.setFechaFin(LocalDate.now());

		assertThrows(IllegalArgumentException.class, () -> convocatoriaService.crear(c));
	}

	@Test
	void testCrearThrowsIllegalArgument_nullDates() {
		Convocatoria c = new Convocatoria();
		c.setNombreConvocatoria("Valida");

		assertThrows(IllegalArgumentException.class, () -> convocatoriaService.crear(c));
	}

	@Test
	void testCrearThrowsIllegalArgument_startAfterEnd() {
		Convocatoria c = new Convocatoria();
		c.setNombreConvocatoria("FechasInvalidas");
		c.setFechaInicio(LocalDate.of(2025, 12, 31));
		c.setFechaFin(LocalDate.of(2025, 1, 1));

		assertThrows(IllegalArgumentException.class, () -> convocatoriaService.crear(c));
	}

	@Test
	void testCrearThrowsAlreadyExists() {
		Convocatoria c = new Convocatoria();
		c.setNombreConvocatoria("Repetida");
		c.setFechaInicio(LocalDate.now());
		c.setFechaFin(LocalDate.now());

		when(convocatoriaRepository.existsByNombreConvocatoria("Repetida")).thenReturn(true);

		assertThrows(AlreadyExistsException.class, () -> convocatoriaService.crear(c));
	}

	@Test
	void testBorrarTodos() {
		when(convocatoriaRepository.count()).thenReturn(5L);

		int deleted = convocatoriaService.borrarTodos();

		verify(convocatoriaRepository, times(1)).deleteAll();
		assertEquals(5, deleted);
	}

	@Test
	void testObtenerConMasDeSolicitudesSuccess() {
		List<Integer> ids = List.of(1, 2);
		List<Convocatoria> convocatorias = List.of(new Convocatoria(), new Convocatoria());

		when(solicitudRepository.findConvocatoriaIdsConMasDeSolicitudes(10)).thenReturn(ids);
		when(convocatoriaRepository.findAllById(ids)).thenReturn(convocatorias);

		List<Convocatoria> result = convocatoriaService.obtenerConMasDeSolicitudes(10);

		assertEquals(2, result.size());
	}

	@Test
	void testObtenerConMasDeSolicitudesThrowsNotFound() {
		when(solicitudRepository.findConvocatoriaIdsConMasDeSolicitudes(100)).thenReturn(Collections.emptyList());

		NotFoundException ex = assertThrows(NotFoundException.class,
				() -> convocatoriaService.obtenerConMasDeSolicitudes(100));

		assertEquals("No hay convocatorias con más de 100 solicitudes.", ex.getMessage());
	}

	@Test
	void testListarFuturasSuccess() {
		LocalDate hoy = LocalDate.now();

		Convocatoria futura1 = new Convocatoria();
		futura1.setId(1);
		futura1.setNombreConvocatoria("Futura 1");
		futura1.setFechaInicio(hoy.plusDays(3));
		futura1.setFechaFin(hoy.plusDays(10));

		when(convocatoriaRepository.findByFechaInicioAfter(hoy)).thenReturn(List.of(futura1));

		List<Convocatoria> result = convocatoriaService.listarFuturas();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Futura 1", result.get(0).getNombreConvocatoria());
		verify(convocatoriaRepository, times(1)).findByFechaInicioAfter(hoy);
	}

	@Test
	void testListarFuturasThrowsNotFound() {
		LocalDate hoy = LocalDate.now();

		when(convocatoriaRepository.findByFechaInicioAfter(hoy)).thenReturn(Collections.emptyList());

		NotFoundException ex = assertThrows(NotFoundException.class, () -> convocatoriaService.listarFuturas());
		assertEquals("No hay convocatorias futuras.", ex.getMessage());
		verify(convocatoriaRepository, times(1)).findByFechaInicioAfter(hoy);
	}

}
