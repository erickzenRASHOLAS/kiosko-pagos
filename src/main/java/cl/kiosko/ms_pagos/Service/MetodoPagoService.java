package cl.kiosko.ms_pagos.Service;

import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.Model.MetodoPago;
import cl.kiosko.ms_pagos.Repository.MetodoPagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true) //esto va para optimización no importa realmente en este nivel de codigo pero es buena practica
//si se quiere agregar un metodo que guarde debe llevar @Transactional solamente (sin true)
public class MetodoPagoService {
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    private MetodoPagoResponseDTO makeToMetodoPagoResponseDTO(MetodoPago metodoPago) {
        log.error("Se Formatea de MetodoPago a DTO");
        return new MetodoPagoResponseDTO(
                metodoPago.getMetodoPagoId(),
                metodoPago.getTipoPago()
        );
    }

    // Como no hay RequestDTO, recibimos la entidad MetodoPago directamente
    public MetodoPagoResponseDTO saveMetodoPago(MetodoPago metodoPago) {
        log.error("Se Guarda MetodoPago");
        // metodoPago ya viene con los datos desde el Controller
        MetodoPago guardado = metodoPagoRepository.save(metodoPago);
        return makeToMetodoPagoResponseDTO(guardado);
    }

    public List<MetodoPagoResponseDTO> listMetodoPago() {
        log.error("Se Listan todos los Metodos de Pago");
        return metodoPagoRepository.findAll().stream().map(this::makeToMetodoPagoResponseDTO).toList();
    }

    // integer porque el id es int no Long
    public MetodoPagoResponseDTO findMetodoPagoDTO(Integer id) {
        log.error("Se busca el Metodo de Pago de ID {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id).orElse(null);
        return (metodoPago != null) ? makeToMetodoPagoResponseDTO(metodoPago) : null;
    }

    // Como no hay RequestDTO, recibimos la entidad MetodoPago directamente
    public MetodoPagoResponseDTO updateMetodoPago(Integer id, MetodoPago metodoPagoDatos) {
        log.error("Se actualiza el Metodo de Pago de ID {}", id);
        MetodoPago metodoAModificar = metodoPagoRepository.findById(id).orElse(null);
        if (metodoAModificar != null) {
            metodoAModificar.setTipoPago(metodoPagoDatos.getTipoPago());

            MetodoPago actualizado = metodoPagoRepository.save(metodoAModificar);
            return makeToMetodoPagoResponseDTO(actualizado);
        }
        return null;
    }

    public void deleteMetodoPago(Integer id) {
        log.error("Se elimina el Metodo de Pago de ID {}", id);
        if (metodoPagoRepository.existsById(id)) {
            metodoPagoRepository.deleteById(id);
        } else {
            throw new java.util.NoSuchElementException("No se puede eliminar. El metodo de pago con ID " + id + " no existe.");
        }
    }
}
