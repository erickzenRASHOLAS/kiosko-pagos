package cl.kiosko.ms_pagos.repository;


import cl.kiosko.ms_pagos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Spring Data JPA analiza el nombre de este mét odo ("findBy" + "Email")
     * y genera automáticamente la consulta: SELECT * FROM _user WHERE email = ?
     * * Usamos Optional<User> para manejar de forma limpia y funcional (con .orElseThrow())
     * los casos donde el usuario no exista en la base de datos.
     */
    Optional<User> findByEmail(String email);

}