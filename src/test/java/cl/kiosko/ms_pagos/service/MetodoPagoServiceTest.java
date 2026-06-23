package cl.kiosko.ms_pagos.service;

import cl.kiosko.ms_pagos.DTO.MetodoPagoRequestDTO;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.model.MetodoPago;
import cl.kiosko.ms_pagos.repository.MetodoPagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetodoPagoServiceTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @InjectMocks
    private MetodoPagoService metodoPagoService;

    private MetodoPago metodoPagoMock;
    private MetodoPagoRequestDTO requestDTOMock;

    @BeforeEach
    void setUp() {
        // Datos de prueba que se reinician antes de cada test
        metodoPagoMock = new MetodoPago(1, "TARJETA DE CREDITO");
        requestDTOMock = new MetodoPagoRequestDTO();
        requestDTOMock.setTipoPago("TARJETA DE CREDITO");
    }

    @Test
    void saveMetodoPago_DeberiaGuardarYRetornarDTO() {
        // Arrange (Preparar)
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenReturn(metodoPagoMock);

        // Act (Ejecutar)
        MetodoPagoResponseDTO result = metodoPagoService.saveMetodoPago(requestDTOMock);

        // Assert (Comprobar)
        assertNotNull(result);
        assertEquals("TARJETA DE CREDITO", result.getTipoPago());
        verify(metodoPagoRepository, times(1)).save(any(MetodoPago.class));
    }

    @Test
    void listMetodoPago_DeberiaRetornarListaDeDTOs() {
        when(metodoPagoRepository.findAll()).thenReturn(Arrays.asList(metodoPagoMock));

        List<MetodoPagoResponseDTO> result = metodoPagoService.listMetodoPago();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(metodoPagoRepository, times(1)).findAll();
    }

    @Test
    void findMetodoPagoDTO_CuandoExiste_DeberiaRetornarDTO() {
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));

        MetodoPagoResponseDTO result = metodoPagoService.findMetodoPagoDTO(1);

        assertNotNull(result);
        assertEquals(1, result.getMetodoPagoId());
    }

    @Test
    void findMetodoPagoDTO_CuandoNoExiste_DeberiaRetornarNull() {
        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        MetodoPagoResponseDTO result = metodoPagoService.findMetodoPagoDTO(99);

        assertNull(result);
    }

    @Test
    void updateMetodoPago_CuandoExiste_DeberiaActualizar() {
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenReturn(metodoPagoMock);

        MetodoPagoResponseDTO result = metodoPagoService.updateMetodoPago(1, requestDTOMock);

        assertNotNull(result);
        verify(metodoPagoRepository, times(1)).save(metodoPagoMock);
    }

    @Test
    void deleteMetodoPago_CuandoExiste_DeberiaEliminar() {
        when(metodoPagoRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> metodoPagoService.deleteMetodoPago(1));
        verify(metodoPagoRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteMetodoPago_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(metodoPagoRepository.existsById(99)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> metodoPagoService.deleteMetodoPago(99));
        verify(metodoPagoRepository, never()).deleteById(anyInt());
    }
}