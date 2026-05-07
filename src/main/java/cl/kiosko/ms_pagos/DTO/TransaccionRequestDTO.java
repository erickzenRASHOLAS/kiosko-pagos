package cl.kiosko.ms_pagos.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionRequestDTO {
    @NotNull(message = "El ID de la venta es obligatorio")
    private Long ventaId;

    @NotNull(message = "El ID del método de pago es obligatorio")
    private Long metodoPagoId;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto no puede ser negativo")
    private Double monto;
}
