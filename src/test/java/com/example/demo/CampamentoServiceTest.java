package com.example.demo;

import entity.Campamento;
import entity.Destino;
import exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CampamentoRepository;
import service.CampamentoService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampamentoServiceTest {

    @Mock
    private CampamentoRepository repo;

    @InjectMocks
    private CampamentoService service;

    private Campamento campamento;

    @BeforeEach
    void setUp() {
        campamento = new Campamento();
        campamento.setId(1);
        campamento.setNombre("Campamento Verano");
        campamento.setDestino(Destino.Nacional);
    }

    @Test
    void testCrear_ok() {
        when(repo.save(any(Campamento.class))).thenReturn(campamento);

        Campamento creado = service.crear(campamento);

        assertNotNull(creado);
        assertEquals("Campamento Verano", creado.getNombre());
        verify(repo).save(any(Campamento.class));
    }

    @Test
    void testListarTodos_vacio_lanzaExcepcion() {
        when(repo.findAll()).thenReturn(List.of());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            service.listarTodos();
        });

        assertEquals("No existen campamentos registrados.", ex.getMessage());
    }

    @Test
    void testObtenerPorId_noExiste_lanzaNotFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.obtenerPorId(1));
    }

    @Test
    void testActualizar_ok() {
        when(repo.findById(1)).thenReturn(Optional.of(campamento));
        when(repo.save(any(Campamento.class))).thenReturn(campamento);

        Campamento actualizado = new Campamento();
        actualizado.setNombre("Campamento Actualizado");
        actualizado.setDestino(Destino.Internacional);

        Campamento result = service.actualizar(1, actualizado);

        assertEquals("Campamento Actualizado", result.getNombre());
        assertEquals(Destino.Internacional, result.getDestino());
    }

    @Test
    void testBorrar_ok() {
        when(repo.findById(1)).thenReturn(Optional.of(campamento));

        service.borrar(1);

        verify(repo).delete(campamento);
    }
}
