package cl.kiosko.ms_pagos.Repository;

import cl.kiosko.ms_pagos.Model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<MetodoPago, Long> {
}
