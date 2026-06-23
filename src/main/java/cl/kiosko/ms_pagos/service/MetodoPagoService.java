package cl.kiosko.ms_pagos.service;

import cl.kiosko.ms_pagos.DTO.MetodoPagoRequestDTO;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.model.MetodoPago;
import cl.kiosko.ms_pagos.repository.MetodoPagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MetodoPagoService {
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    private MetodoPagoResponseDTO makeToMetodoPagoResponseDTO(MetodoPago metodoPago) {
        log.info("Se formatea de MetodoPago a DTO"); // Cambiado a info
        return new MetodoPagoResponseDTO(
                metodoPago.getMetodoPagoId(),
                metodoPago.getTipoPago()
        );
    }


// AHORA RECIBE EL DTO (Esto quita el error rojo de tu Controller)
    public MetodoPagoResponseDTO saveMetodoPago(MetodoPagoRequestDTO dto) {
        log.info("Se guarda nuevo MetodoPago: {}", dto.getTipoPago());

        MetodoPago metodo = new MetodoPago();
        metodo.setTipoPago(dto.getTipoPago());

        MetodoPago guardado = metodoPagoRepository.save(metodo);
        return makeToMetodoPagoResponseDTO(guardado);
    }

    public List<MetodoPagoResponseDTO> listMetodoPago() {
        log.info("Se listan todos los Metodos de Pago");
        return metodoPagoRepository.findAll().stream().map(this::makeToMetodoPagoResponseDTO).toList();
    }

    // integer porque el id es int no Long
    public MetodoPagoResponseDTO findMetodoPagoDTO(Integer id) {
        log.info("Se busca el Metodo de Pago de ID {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id).orElse(null);
        return (metodoPago != null) ? makeToMetodoPagoResponseDTO(metodoPago) : null;
    }

    // Como no hay RequestDTO, recibimos la entidad MetodoPago directamente
    // AHORA RECIBE EL DTO
    public MetodoPagoResponseDTO updateMetodoPago(Integer id, MetodoPagoRequestDTO dto) {
        log.info("Se actualiza el Metodo de Pago de ID {}", id);
        MetodoPago metodoAModificar = metodoPagoRepository.findById(id).orElse(null);

        if (metodoAModificar != null) {
            metodoAModificar.setTipoPago(dto.getTipoPago()); // Usamos el getTipoPago() del DTO

            MetodoPago actualizado = metodoPagoRepository.save(metodoAModificar);
            return makeToMetodoPagoResponseDTO(actualizado);
        }
        return null;
    }

    public void deleteMetodoPago(Integer id) {
        log.info("Se elimina el Metodo de Pago de ID {}", id);
        if (metodoPagoRepository.existsById(id)) {
            metodoPagoRepository.deleteById(id);
        } else {
            throw new java.util.NoSuchElementException("No se puede eliminar. El metodo de pago con ID " + id + " no existe.");
        }
    }
}
