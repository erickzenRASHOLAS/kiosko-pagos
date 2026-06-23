package cl.kiosko.ms_pagos.repository;

import cl.kiosko.ms_pagos.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
