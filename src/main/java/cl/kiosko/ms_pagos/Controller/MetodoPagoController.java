package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.DTO.MetodoPagoResponseDTO;
import cl.kiosko.ms_pagos.Model.MetodoPago;
import cl.kiosko.ms_pagos.Service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/metodo_pagos")
public class MetodoPagoController {
    @Autowired
    private MetodoPagoService metodoPagoService;

    @PostMapping("")
    public ResponseEntity<MetodoPagoResponseDTO> agregarMetodoPago(@RequestBody MetodoPago metodoPago) {
        // Como no hay RequestDTO, recibimos y pasamos la entidad MetodoPago directamente
        MetodoPagoResponseDTO nuevoMetodo = metodoPagoService.saveMetodoPago(metodoPago);
        // Retornamos el DTO con estado Created
        return new ResponseEntity<>(nuevoMetodo, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<MetodoPagoResponseDTO>> listarMetodosPago() {
        List<MetodoPagoResponseDTO> metodos = metodoPagoService.listMetodoPago();

        // Si no hay nada retorna un noContent
        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(metodos);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> buscarMetodoPagoId(@PathVariable Integer id) { // Recuerda que el ID es Integer
        MetodoPagoResponseDTO metodo = metodoPagoService.findMetodoPagoDTO(id);

        //si no encuentra el metodo pago tira una exepción
        if (metodo == null) {
            throw new NoSuchElementException("No existe el método de pago con Id: " + id);
        } else {
            return ResponseEntity.ok(metodo);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> actualizarMetodoPago(@PathVariable Integer id, @RequestBody MetodoPago metodoPago) { // ID Integer y sin RequestDTO
        MetodoPagoResponseDTO actualizado = metodoPagoService.updateMetodoPago(id, metodoPago);

        //igual que antes si no existe, exepción
        if (actualizado == null) {
            throw new NoSuchElementException("No se puede actualizar. El método de pago con ID " + id + " no existe.");
        } else {
            return ResponseEntity.ok(actualizado);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMetodoPagoId(@PathVariable Integer id) { // Recuerda que el ID es Integer
        //si no existe salta la expeción desde el service
        metodoPagoService.deleteMetodoPago(id);
        //si NO salta la expeción se borra
        return ResponseEntity.noContent().build();
    }

}
