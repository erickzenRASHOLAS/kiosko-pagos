package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.Assembler.TransaccionAssembler;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.DTO.TransaccionRequestDTO;
import cl.kiosko.ms_pagos.DTO.TransaccionResponseDTO;
import cl.kiosko.ms_pagos.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Mantenemos los filtros apagados por seguridad JWT
public class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransaccionService transaccionService;

    @MockitoBean
    private TransaccionAssembler assembler;

    private ObjectMapper objectMapper;

    private TransaccionResponseDTO responseDTO;
    private TransaccionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();

        // 1. Preparamos el DTO anidado del Método de Pago
        MetodoPagoResponseDTO metodoPagoResponse = new MetodoPagoResponseDTO(1, "DEBITO");

        // 2. Preparamos el DTO de Respuesta
        responseDTO = new TransaccionResponseDTO();
        responseDTO.setTransaccionId(1L);
        responseDTO.setVentaId(100L);
        responseDTO.setMonto(15000.0);
        responseDTO.setEstado("COMPLETADO");
        responseDTO.setMetodoPago(metodoPagoResponse); // Anidamos el objeto

        // 3. Preparamos el DTO de Petición (Request)
        requestDTO = new TransaccionRequestDTO();
        requestDTO.setVentaId(100L);
        requestDTO.setMonto(15000.0);
        requestDTO.setEstado("COMPLETADO");
        requestDTO.setMetodoPagoId(1);

        when(assembler.toModel(any(TransaccionResponseDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // PRUEBAS DE LISTAR (GET)

    @Test
    void listarTransacciones_CuandoHayDatos_DeberiaRetornar200() throws Exception {
        // Asumiendo que tu método en el service se llama listTransaccion()
        when(transaccionService.listTransaccion()).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(get("/transacciones")) // Verifica que tu endpoint sea /transacciones
                .andExpect(status().isOk());
    }

    @Test
    void listarTransacciones_CuandoEstaVacio_DeberiaRetornar204() throws Exception {
        when(transaccionService.listTransaccion()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/transacciones"))
                .andExpect(status().isNoContent()); // Cambia a isOk() si devuelves 200 con lista vacía
    }

    // PRUEBAS DE BUSCAR POR ID (GET)

    @Test
    void buscarTransaccionId_CuandoExiste_DeberiaRetornar200() throws Exception {
        // Asumiendo que tu método en el service se llama findTransaccionDTO
        when(transaccionService.findTransaccionDTO(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/transacciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"))
                .andExpect(jsonPath("$.monto").value(15000.0));
    }

    @Test
    void buscarTransaccionId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(transaccionService.findTransaccionDTO(99L)).thenReturn(null);

        mockMvc.perform(get("/transacciones/99"))
                .andExpect(status().isNotFound());
    }

    // PRUEBAS DE CREAR (POST)

    @Test
    void crearTransaccion_DeberiaRetornar201() throws Exception {
        when(transaccionService.saveTransaccion(any(TransaccionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    void crearTransaccion_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        // Rompemos las validaciones a propósito para probar el @Valid
        TransaccionRequestDTO badRequestDTO = new TransaccionRequestDTO();
        badRequestDTO.setMonto(-500.0); // Falla el @Min(0)
        badRequestDTO.setEstado("");    // Falla el @NotBlank

        mockMvc.perform(post("/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    // PRUEBAS DE ACTUALIZAR (PUT)

    @Test
    void actualizarTransaccion_CuandoExiste_DeberiaRetornar200() throws Exception {
        when(transaccionService.updateTransaccion(eq(1L), any(TransaccionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/transacciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarTransaccion_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(transaccionService.updateTransaccion(eq(99L), any(TransaccionRequestDTO.class))).thenReturn(null);

        mockMvc.perform(put("/transacciones/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarTransaccion_DeberiaRetornar204() throws Exception {
        doNothing().when(transaccionService).deleteTransaccion(1L);

        mockMvc.perform(delete("/transacciones/1"))
                .andExpect(status().isNoContent()); // Cambia a isOk() si tu API devuelve 200 al borrar
    }
}