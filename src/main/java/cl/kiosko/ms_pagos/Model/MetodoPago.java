package cl.kiosko.ms_pagos.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "metodo_pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_pago_id")
    private int metodoPagoId;

    @NotBlank(message = "el tipo de pago no puede estar vacio")
    @Column(name = "tipo_pago")
    private String tipoPago;

}
