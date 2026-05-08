package cl.kiosko.ms_pagos.Repository;

import cl.kiosko.ms_pagos.Model.MetodoPago;
import cl.kiosko.ms_pagos.Model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
