package cl.kiosko.ms_pagos.Assembler;

import cl.kiosko.ms_pagos.Controller.MetodoPagoController;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetodoPagoAssembler implements RepresentationModelAssembler<MetodoPagoResponseDTO, MetodoPagoResponseDTO> {

    @Override
    public MetodoPagoResponseDTO toModel(MetodoPagoResponseDTO dto) {
        // Enlace al propio recurso (self)
        dto.add(linkTo(methodOn(MetodoPagoController.class).buscarMetodoPagoId(dto.getMetodoPagoId())).withSelfRel());

        // Enlace a la colección completa
        dto.add(linkTo(methodOn(MetodoPagoController.class).listarMetodosPago()).withRel("todos-metodos-pago"));

        return dto;
    }
}