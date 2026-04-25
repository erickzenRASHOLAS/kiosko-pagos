package cl.kiosko.ms_pagos.Service;

import cl.kiosko.ms_pagos.Model.Pago;
import cl.kiosko.ms_pagos.Repository.PagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    private List<Pago> listPagos(){
        return pagoRepository.findAll();
    }
}
