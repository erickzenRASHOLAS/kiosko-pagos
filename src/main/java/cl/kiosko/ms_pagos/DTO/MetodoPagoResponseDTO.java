package cl.kiosko.ms_pagos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoResponseDTO {
    private Integer metodoPagoId;
    private String tipoPago;
}