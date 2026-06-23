package cl.kiosko.ms_pagos.Assembler;

import cl.kiosko.ms_pagos.Controller.TransaccionController;
import cl.kiosko.ms_pagos.DTO.TransaccionResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransaccionAssembler implements RepresentationModelAssembler<TransaccionResponseDTO, TransaccionResponseDTO> {

    @Override
    public TransaccionResponseDTO toModel(TransaccionResponseDTO dto) {
        // Enlace al propio recurso (self)
        dto.add(linkTo(methodOn(TransaccionController.class).buscarTransaccionPorId(dto.getTransaccionId())).withSelfRel());

        // Enlace a la colección completa
        dto.add(linkTo(methodOn(TransaccionController.class).listarTransacciones()).withRel("todas-transacciones"));

        // Ejemplo de enlace lógico basado en el estado (opcional pero muy útil)
        if ("PENDIENTE".equals(dto.getEstado())) {
            // Si está pendiente, indicamos que se puede actualizar
            dto.add(linkTo(methodOn(TransaccionController.class).actualizarTransaccion(dto.getTransaccionId(), null)).withRel("actualizar-estado"));
        }

        return dto;
    }
}