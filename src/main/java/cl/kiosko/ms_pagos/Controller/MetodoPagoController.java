package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.DTO.MetodoPagoRequestDTO;
import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.Service.MetodoPagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/metodo_pagos")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @PostMapping("")
    public ResponseEntity<MetodoPagoResponseDTO> agregarMetodoPago(@Valid @RequestBody MetodoPagoRequestDTO dto) {
        // Ahora usamos el RequestDTO con @Valid en lugar de la entidad directa
        MetodoPagoResponseDTO nuevoMetodo = metodoPagoService.saveMetodoPago(dto);
        return new ResponseEntity<>(nuevoMetodo, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<MetodoPagoResponseDTO>> listarMetodosPago() {
        List<MetodoPagoResponseDTO> metodos = metodoPagoService.listMetodoPago();

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(metodos);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> buscarMetodoPagoId(@PathVariable Integer id) {
        MetodoPagoResponseDTO metodo = metodoPagoService.findMetodoPagoDTO(id);

        if (metodo == null) {
            throw new NoSuchElementException("No existe el método de pago con Id: " + id);
        } else {
            return ResponseEntity.ok(metodo);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPagoRequestDTO dto) {
        // Al igual que en POST, usamos el RequestDTO y @Valid
        MetodoPagoResponseDTO actualizado = metodoPagoService.updateMetodoPago(id, dto);

        if (actualizado == null) {
            throw new NoSuchElementException("No se puede actualizar. El método de pago con ID " + id + " no existe.");
        } else {
            return ResponseEntity.ok(actualizado);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMetodoPagoId(@PathVariable Integer id) {
        metodoPagoService.deleteMetodoPago(id);
        return ResponseEntity.noContent().build();
    }
}