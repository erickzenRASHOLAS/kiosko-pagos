package cl.kiosko.ms_pagos.Repository;

import cl.kiosko.ms_pagos.Model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PagoRepository  extends JpaRepository<Pago, Long> {
}
