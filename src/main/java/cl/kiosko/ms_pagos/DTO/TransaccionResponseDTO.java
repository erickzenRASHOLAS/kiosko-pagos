package cl.kiosko.ms_pagos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO extends RepresentationModel<TransaccionResponseDTO> {
    private Long transaccionId;
    private Long ventaId;
    private Double monto;
    private String estado; // "COMPLETADO", "RECHAZADO", etc, esto se deberia validad

    // se anida el DTO de respuesta del metodo de pago
    private MetodoPagoResponseDTO metodoPago;
}
