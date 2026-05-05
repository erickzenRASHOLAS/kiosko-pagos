package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.Service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pagos")
public class MetodoPagoController {
    @Autowired
    private MetodoPagoService pagoService;

}
