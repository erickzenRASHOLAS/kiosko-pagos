package cl.kiosko.ms_pagos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoResponseDTO {
    private Long metodoPagoId;
    private String tipoPago;
}