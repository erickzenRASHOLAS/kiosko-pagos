package cl.kiosko.ms_pagos.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/v1/payments")
public class PaymentsController {

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory() {
        // Si logras ver este JSON, significa que tu JWT y Filtro funcionan perfecto
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "¡Acceso concedido! El filtro JWT validó tu token correctamente.",
                "data", "Historial de pagos del Kiosko"
        ));
    }
}
