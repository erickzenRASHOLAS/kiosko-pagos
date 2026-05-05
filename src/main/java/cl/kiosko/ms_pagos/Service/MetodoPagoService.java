package cl.kiosko.ms_pagos.Service;

import cl.kiosko.ms_pagos.Model.MetodoPago;
import cl.kiosko.ms_pagos.Repository.MetodoPagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MetodoPagoService {
    @Autowired
    private MetodoPagoRepository pagoRepository;

    private List<MetodoPago> listPagos(){
        return pagoRepository.findAll();
    }
}
