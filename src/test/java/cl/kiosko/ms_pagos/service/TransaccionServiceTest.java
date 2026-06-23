package cl.kiosko.ms_pagos.service;

import cl.kiosko.ms_pagos.DTO.TransaccionRequestDTO;
import cl.kiosko.ms_pagos.DTO.TransaccionResponseDTO;
import cl.kiosko.ms_pagos.model.MetodoPago;
import cl.kiosko.ms_pagos.model.Transaccion;
import cl.kiosko.ms_pagos.repository.MetodoPagoRepository;
import cl.kiosko.ms_pagos.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @Mock
    private WebClient webClient; // Aunque no se use en el método activo, debe estar para el InjectMocks

    @InjectMocks
    private TransaccionService transaccionService;

    private Transaccion transaccionMock;
    private MetodoPago metodoPagoMock;
    private TransaccionRequestDTO requestDTOMock;

    @BeforeEach
    void setUp() {
        metodoPagoMock = new MetodoPago(1, "EFECTIVO");
        transaccionMock = new Transaccion(1L, 100L, 5000.0, "COMPLETADO", metodoPagoMock);

        requestDTOMock = new TransaccionRequestDTO();
        requestDTOMock.setVentaId(100L);
        requestDTOMock.setMonto(5000.0);
        requestDTOMock.setMetodoPagoId(1);
    }

    @Test
    void saveTransaccion_DeberiaGuardarCorrectamente() {
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionMock);

        TransaccionResponseDTO result = transaccionService.saveTransaccion(requestDTOMock);

        assertNotNull(result);
        assertEquals("COMPLETADO", result.getEstado());
        assertEquals(5000.0, result.getMonto());
        assertNotNull(result.getMetodoPago());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void saveTransaccion_MetodoPagoNoExiste_DeberiaLanzarExcepcion() {
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> transaccionService.saveTransaccion(requestDTOMock));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void listTransaccion_DeberiaRetornarLista() {
        when(transaccionRepository.findAll()).thenReturn(Arrays.asList(transaccionMock));

        List<TransaccionResponseDTO> result = transaccionService.listTransaccion();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateTransaccion_CuandoExiste_DeberiaActualizar() {
        requestDTOMock.setEstado("PENDIENTE");
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccionMock));
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionMock);

        TransaccionResponseDTO result = transaccionService.updateTransaccion(1L, requestDTOMock);

        assertNotNull(result);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void deleteTransaccion_CuandoExiste_DeberiaEliminar() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> transaccionService.deleteTransaccion(1L));
        verify(transaccionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTransaccion_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(transaccionRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> transaccionService.deleteTransaccion(99L));
        verify(transaccionRepository, never()).deleteById(anyLong());
    }
}