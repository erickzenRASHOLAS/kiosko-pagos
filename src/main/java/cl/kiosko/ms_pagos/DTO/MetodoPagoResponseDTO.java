package cl.kiosko.ms_pagos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoResponseDTO extends RepresentationModel<MetodoPagoResponseDTO> {
    private Integer metodoPagoId;
    private String tipoPago;
}