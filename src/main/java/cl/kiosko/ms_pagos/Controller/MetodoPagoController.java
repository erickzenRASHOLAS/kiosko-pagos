package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.Assembler.MetodoPagoAssembler;
import cl.kiosko.ms_pagos.DTO.MetodoPagoRequestDTO;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.service.MetodoPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/metodo_pagos")
@Tag(name="Metodos de Pago", description = "Operaciones relacionadas con los Metodos de Pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;
    @Autowired
    private MetodoPagoAssembler assembler;

    @PostMapping("")
    @Operation(summary = "Agregar Metodo de Pago", description = "Se agrega/crea un nuevo metodo de pago")
    public ResponseEntity<MetodoPagoResponseDTO> agregarMetodoPago(@Valid @RequestBody MetodoPagoRequestDTO dto) {
        // Ahora usamos el RequestDTO con @Valid en lugar de la entidad directa
        MetodoPagoResponseDTO nuevoMetodo = metodoPagoService.saveMetodoPago(dto);
        return new ResponseEntity<>(assembler.toModel(nuevoMetodo), HttpStatus.CREATED);
    }

    @GetMapping("")
    @Operation(summary = "Listar Metodos de pago", description = "Busca y muestra Todos los metodos de pago Existentes")
    public ResponseEntity<CollectionModel<MetodoPagoResponseDTO>> listarMetodosPago() {
        List<MetodoPagoResponseDTO> metodos = metodoPagoService.listMetodoPago();

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            // Se transforma la lista y se agrega el link global
            List<MetodoPagoResponseDTO> metodosConLinks = metodos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            CollectionModel<MetodoPagoResponseDTO> collectionModel = CollectionModel.of(metodosConLinks,
                    linkTo(methodOn(MetodoPagoController.class).listarMetodosPago()).withSelfRel());

            return ResponseEntity.ok(collectionModel);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Metodo de pago por Id", description = "Se introduce el ID del metodo de pago y lo muestra (Si es que este metodo existe)")
    public ResponseEntity<MetodoPagoResponseDTO> buscarMetodoPagoId(@PathVariable Integer id) {
        MetodoPagoResponseDTO metodo = metodoPagoService.findMetodoPagoDTO(id);

        if (metodo == null) {
            throw new NoSuchElementException("No existe el método de pago con Id: " + id);
        } else {
            return ResponseEntity.ok(assembler.toModel(metodo));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Metodo de pago", description = "Se actualiza el metodo de pago según su ID(se debe poner en la URL) luego se deben enviar los nuevos parametros en JSON")
    public ResponseEntity<MetodoPagoResponseDTO> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPagoRequestDTO dto) {
        // Al igual que en POST, usamos el RequestDTO y @Valid
        MetodoPagoResponseDTO actualizado = metodoPagoService.updateMetodoPago(id, dto);

        if (actualizado == null) {
            throw new NoSuchElementException("No se puede actualizar. El método de pago con ID " + id + " no existe.");
        } else {
            return ResponseEntity.ok(assembler.toModel(actualizado));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Metodo de pago", description = "Elimina un metodo de pago según su ID (se debe poner en la URL)")
    public ResponseEntity<Void> eliminarMetodoPagoId(@PathVariable Integer id) {
        metodoPagoService.deleteMetodoPago(id);
        return ResponseEntity.noContent().build();
    }
}