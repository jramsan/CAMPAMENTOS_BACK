package com.example.demo;

import entity.Solicitante;
import exception.AlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.SolicitanteRepository;
import service.SolicitanteService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitanteServiceTest {

    @Mock
    private SolicitanteRepository repo;

    @InjectMocks
    private SolicitanteService service;

    // === PÉGALO AQUÍ ===
    @Test
    void crear_cuandoYaExiste_lanzaAlreadyExistsException() {
        // Arrange
        String dniExistente = "12345678A";
        Solicitante s = new Solicitante();
        s.setDni(dniExistente);
        s.setNombre("Juan");
        s.setApellido1("Pérez");

        // El repositorio indica que ya existe ese DNI
        when(repo.existsByDni(dniExistente)).thenReturn(true);

        // Act + Assert
        assertThrows(AlreadyExistsException.class, () -> service.crear(s));
    }
    @Test
    void crear_cuandoNoExiste_guardaYDevuelveSolicitante() {
        // Arrange
        Solicitante s = new Solicitante();
        s.setDni("98765432B");
        s.setNombre("María");
        s.setApellido1("Gómez");

        // Simulamos que el repositorio indica que NO existe
        when(repo.existsByDni("98765432B")).thenReturn(false);

        // Simulamos el guardado (el repo devuelve el mismo solicitante)
        when(repo.save(any(Solicitante.class))).thenReturn(s);

        // Act
        Solicitante resultado = service.crear(s);

        // Assert
        assertEquals("María", resultado.getNombre());
        assertEquals("98765432B", resultado.getDni());
        verify(repo).existsByDni("98765432B"); // se comprueba existencia
        verify(repo, times(1)).save(s);        // se guarda una vez
    }
    // === HASTA AQUÍ ===
}
