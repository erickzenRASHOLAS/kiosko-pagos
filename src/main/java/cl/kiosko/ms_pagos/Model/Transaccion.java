package cl.kiosko.ms_pagos.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="transaccion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {
    //EVITAR TILDES EN LOS NOMBRES PARA EVITAR ALGÚN ERROR
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "transaccion_id")
    private Long transaccionId;
    /*@NotNull
    @Column(name="")
    private Long ventaId; // ID de la venta que deberia venir de ms-ventas ¿preguntar al profe por esto?
    */
    @NotNull
    @Column(name = "monto_transaccion")
    private Double monto;
    @NotNull
    @Column(name="estado_transaccion")
    private String estado; // "PENDIENTE", "COMPLETADO", "RECHAZADO"

    //relación
    @ManyToOne
    @JoinColumn(name = "metodo_pago_id")
    private MetodoPago metodoPago;

}