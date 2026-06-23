package cl.kiosko.ms_pagos.Config; // Ajusta tu paquete

import cl.kiosko.ms_pagos.model.MetodoPago;
import cl.kiosko.ms_pagos.model.Transaccion;
import cl.kiosko.ms_pagos.repository.MetodoPagoRepository;
import cl.kiosko.ms_pagos.repository.TransaccionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

//DATA FAKER CREADO POR IA

@Configuration
public class DataFakerConfig {

    @Bean
    CommandLineRunner iniciarDatosFalsos(MetodoPagoRepository metodoPagoRepository, TransaccionRepository transaccionRepository) {
        return args -> {
            // 1. POBLAR METODOS DE PAGO (Regla de negocio)
            if (metodoPagoRepository.count() == 0) {
                System.out.println("⏳ Generando métodos de pago por defecto...");

                List<MetodoPago> metodos = List.of(
                        new MetodoPago(null, "EFECTIVO"),
                        new MetodoPago(null, "DEBITO"),
                        new MetodoPago(null, "CREDITO"),
                        new MetodoPago(null, "TRANSFERENCIA")
                );

                metodoPagoRepository.saveAll(metodos);
                System.out.println("✅ Métodos de pago creados exitosamente.");
            }

            // 2. POBLAR TRANSACCIONES FALSAS
            if (transaccionRepository.count() == 0) {
                System.out.println("⏳ Generando transacciones falsas con Datafaker...");
                Faker faker = new Faker();

                // Traemos los métodos de pago que acabamos de guardar
                // para poder asignarlos de forma aleatoria a cada transacción
                List<MetodoPago> metodosGuardados = metodoPagoRepository.findAll();
                List<String> estadosPosibles = List.of("PENDIENTE", "COMPLETADO", "RECHAZADO");
                List<Transaccion> transaccionesFalsas = new ArrayList<>();

                // Vamos a generar 15 transacciones falsas
                for (int i = 0; i < 15; i++) {
                    Transaccion transaccion = new Transaccion();

                    // Simulamos IDs de ventas del ms-ventas (ej. del 1 al 20)
                    transaccion.setVentaId((long) faker.number().numberBetween(1, 21));

                    // Monto aleatorio con decimales (ej. entre 1000.0 y 50000.0)
                    transaccion.setMonto(faker.number().randomDouble(2, 1000, 50000));

                    // Estado aleatorio
                    String estadoAleatorio = estadosPosibles.get(faker.number().numberBetween(0, estadosPosibles.size()));
                    transaccion.setEstado(estadoAleatorio);

                    // Método de pago aleatorio (saca uno de la lista de los 4 permitidos)
                    MetodoPago metodoAleatorio = metodosGuardados.get(faker.number().numberBetween(0, metodosGuardados.size()));
                    transaccion.setMetodoPago(metodoAleatorio);

                    transaccionesFalsas.add(transaccion);
                }

                // Guardamos todas las transacciones generadas
                transaccionRepository.saveAll(transaccionesFalsas);
                System.out.println("✅ ¡Base de datos poblada exitosamente con 15 transacciones!");

            } else {
                System.out.println("👍 La base de datos ya tiene información en pagos, omitiendo Datafaker.");
            }
        };
    }
}