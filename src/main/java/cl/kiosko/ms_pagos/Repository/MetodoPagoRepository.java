package cl.kiosko.ms_pagos.Repository;

import cl.kiosko.ms_pagos.Model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
}
