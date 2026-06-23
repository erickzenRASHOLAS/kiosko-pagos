
CREATE TABLE metodo_pago (
    metodo_pago_id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago VARCHAR(50) NOT NULL
);

CREATE TABLE transaccion (
    transaccion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    monto_transaccion DOUBLE NOT NULL,
    estado_transaccion VARCHAR(20) NOT NULL,
    metodo_pago_id INT,
    CONSTRAINT fk_metodo_pago
    FOREIGN KEY (metodo_pago_id)
    REFERENCES metodo_pago(metodo_pago_id)
);

CREATE TABLE _user (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50)
);