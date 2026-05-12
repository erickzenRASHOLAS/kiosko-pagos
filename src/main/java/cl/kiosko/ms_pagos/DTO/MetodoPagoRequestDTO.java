package cl.kiosko.ms_pagos.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoRequestDTO {
    @NotBlank(message = "El tipo de pago no puede estar vacío")
    private String tipoPago;
}