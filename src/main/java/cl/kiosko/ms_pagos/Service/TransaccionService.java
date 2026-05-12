package cl.kiosko.ms_pagos.Service; // Revisa que este paquete sea el correcto en tu proyecto

import cl.kiosko.ms_pagos.Model.MetodoPago;
import cl.kiosko.ms_pagos.Model.Transaccion;
import cl.kiosko.ms_pagos.Repository.MetodoPagoRepository;
import cl.kiosko.ms_pagos.Repository.TransaccionRepository;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.DTO.TransaccionRequestDTO;
import cl.kiosko.ms_pagos.DTO.TransaccionResponseDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@Transactional
@Slf4j
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    // INYECTAMOS WEBCLIENT
    @Autowired
    private WebClient webClient;

    // Foramtea a DTO
    private TransaccionResponseDTO makeToTransaccionResponseDTO(Transaccion transaccion) {
        log.info("Se formatea de Transaccion a DTO");

        MetodoPagoResponseDTO metodoDTO = null;
        if (transaccion.getMetodoPago() != null) {
            metodoDTO = new MetodoPagoResponseDTO(
                    transaccion.getMetodoPago().getMetodoPagoId(),
                    transaccion.getMetodoPago().getTipoPago()
            );
        }

        return new TransaccionResponseDTO(
                transaccion.getTransaccionId(),
                transaccion.getVentaId(),
                transaccion.getMonto(),
                transaccion.getEstado(),
                metodoDTO
        );
    }
    /*public TransaccionResponseDTO saveTransaccion(TransaccionRequestDTO dto) {
        log.info("Iniciando creación de transacción para Venta ID: {}", dto.getVentaId());

        // 1. VALIDAR VENTA EN MICROSERVICIO EXTERNO (ms_ventas)
        try {
            webClient.get()
                    .uri("http://localhost:8080/api/v1/ventas/" + dto.getVentaId())
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            log.info("Venta ID {} validada correctamente en ms_ventas", dto.getVentaId());
        } catch (WebClientResponseException.NotFound e) {
            log.error("La venta ID {} no existe en ms_ventas", dto.getVentaId());
            throw new NoSuchElementException("La Venta con ID " + dto.getVentaId() + " no existe. No se puede procesar el pago.");
        } catch (Exception e) {
            log.error("Error al conectar con ms_ventas: ", e);
            throw new RuntimeException("Error de comunicación con el servicio de ventas.");
        }

        // 2. SI EXISTE, PROCEDEMOS A GUARDAR
        Transaccion transaccion = new Transaccion();
        transaccion.setVentaId(dto.getVentaId());
        transaccion.setMonto(dto.getMonto());
        transaccion.setEstado("COMPLETADO");

        if (dto.getMetodoPagoId() != null) {
            MetodoPago metodoPago = metodoPagoRepository.findById(dto.getMetodoPagoId())
                .orElseThrow(() -> new NoSuchElementException("El metodo de pago con ID " + dto.getMetodoPagoId() + " no existe."));
            transaccion.setMetodoPago(metodoPago);
        }

        Transaccion guardada = transaccionRepository.save(transaccion);
        return makeToTransaccionResponseDTO(guardada);
    }*/


    public TransaccionResponseDTO saveTransaccion(TransaccionRequestDTO dto) {
        log.error("Se Guarda de Transaccion a DTO");
        Transaccion transaccion = new Transaccion();
        transaccion.setVentaId(dto.getVentaId());
        transaccion.setMonto(dto.getMonto());
        transaccion.setEstado("COMPLETADO");

        if (dto.getMetodoPagoId() != null) {
            MetodoPago metodoPago = metodoPagoRepository.findById(dto.getMetodoPagoId()).orElse(null);
            if (metodoPago != null) {
                transaccion.setMetodoPago(metodoPago);
            } else {
                throw new java.util.NoSuchElementException("El método de pago con ID " + dto.getMetodoPagoId() + " no existe.");
            }
        }


        Transaccion guardada = transaccionRepository.save(transaccion);
        return makeToTransaccionResponseDTO(guardada);
    }

    public List<TransaccionResponseDTO> listTransaccion(){
        log.error("Se Listan todas las Transacciones");
        return transaccionRepository.findAll().stream().map(this::makeToTransaccionResponseDTO).toList();
    }

    public TransaccionResponseDTO findTransaccionDTO(Long id) {
        log.error("Se busca la Transaccion de ID {}", id);
        Transaccion transaccion = transaccionRepository.findById(id).orElse(null);
        return (transaccion != null) ? makeToTransaccionResponseDTO(transaccion) : null;
    }

    public TransaccionResponseDTO updateTransaccion(Long id, TransaccionRequestDTO dto) {
        log.error("Se actualiza la Transaccion de ID {}", id);
        Transaccion transaccionAModificar = transaccionRepository.findById(id).orElse(null);

        if (transaccionAModificar != null) {
            transaccionAModificar.setVentaId(dto.getVentaId());
            transaccionAModificar.setMonto(dto.getMonto());

            if (dto.getMetodoPagoId() != null) {
                MetodoPago metodoPago = metodoPagoRepository.findById(dto.getMetodoPagoId()).orElse(null);
                if (metodoPago != null) {
                    transaccionAModificar.setMetodoPago(metodoPago);
                } else {
                    throw new java.util.NoSuchElementException("El método de pago con ID " + dto.getMetodoPagoId() + " no existe.");
                }
            }

            Transaccion actualizada = transaccionRepository.save(transaccionAModificar);
            return makeToTransaccionResponseDTO(actualizada);
        }
        return null;
    }

    public void deleteTransaccion(Long id) {
        log.error("Se elimina la Transaccion de ID {}", id);
        if (transaccionRepository.existsById(id)) {
            transaccionRepository.deleteById(id);
        } else {
            throw new java.util.NoSuchElementException("No se puede eliminar. La transaccion con ID " + id + " no existe.");
        }
    }
}