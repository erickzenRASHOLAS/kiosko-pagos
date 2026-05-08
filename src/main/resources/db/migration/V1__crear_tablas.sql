
CREATE TABLE metodo_pago (
    metodo_pago_id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago VARCHAR(50) NOT NULL
);

CREATE TABLE transaccion (
    transaccion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    monto DOUBLE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    metodo_pago_id INT,
    CONSTRAINT fk_metodo_pago
    FOREIGN KEY (metodo_pago_id)
    REFERENCES metodo_pago(metodo_pago_id)
);