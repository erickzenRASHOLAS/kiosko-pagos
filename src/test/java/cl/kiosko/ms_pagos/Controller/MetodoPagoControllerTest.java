package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.Assembler.MetodoPagoAssembler;
import cl.kiosko.ms_pagos.DTO.MetodoPagoRequestDTO;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.service.MetodoPagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(MetodoPagoController.class)
public class MetodoPagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetodoPagoService metodoPagoService;

    @MockitoBean
    private MetodoPagoAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private MetodoPagoResponseDTO responseDTO;
    private MetodoPagoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new MetodoPagoResponseDTO(1, "DEBITO");
        requestDTO = new MetodoPagoRequestDTO();
        requestDTO.setTipoPago("DEBITO");

        // Configuramos el Assembler falso para que simplemente devuelva el mismo DTO
        when(assembler.toModel(any(MetodoPagoResponseDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void agregarMetodoPago_DeberiaRetornar201() throws Exception {
        when(metodoPagoService.saveMetodoPago(any(MetodoPagoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/metodo_pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.metodoPagoId").value(1))
                .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test
    void listarMetodosPago_CuandoHayDatos_DeberiaRetornar200() throws Exception {
        when(metodoPagoService.listMetodoPago()).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(get("/metodo_pagos"))
                .andExpect(status().isOk());
    }

    @Test
    void listarMetodosPago_CuandoEstaVacio_DeberiaRetornar204() throws Exception {
        when(metodoPagoService.listMetodoPago()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/metodo_pagos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarMetodoPagoId_CuandoExiste_DeberiaRetornar200() throws Exception {
        when(metodoPagoService.findMetodoPagoDTO(1)).thenReturn(responseDTO);

        mockMvc.perform(get("/metodo_pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test
    void actualizarMetodoPago_DeberiaRetornar200() throws Exception {
        when(metodoPagoService.updateMetodoPago(eq(1), any(MetodoPagoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/metodo_pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test
    void eliminarMetodoPagoId_DeberiaRetornar204() throws Exception {
        doNothing().when(metodoPagoService).deleteMetodoPago(1);

        mockMvc.perform(delete("/metodo_pagos/1"))
                .andExpect(status().isNoContent());
    }
}