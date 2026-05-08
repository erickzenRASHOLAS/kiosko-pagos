package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.DTO.*;
import cl.kiosko.ms_pagos.Service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;
    //@Valid sirve para que se cumplan los @ que puse anteriormente (ahorra crear if)


    @PostMapping("")
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Valid @RequestBody TransaccionRequestDTO dto) {
        // El service se encarga de la lógica de guardado y vinculación
        TransaccionResponseDTO nuevaTransaccion = transaccionService.saveTransaccion(dto);
        // Retornamos el DTO con estado 201 Created
        return new ResponseEntity<>(nuevaTransaccion, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<TransaccionResponseDTO>> listarTransacciones() {
        List<TransaccionResponseDTO> transacciones = transaccionService.listTransaccion();

        // Si la lista está vacía retornamos 204 No Content
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transacciones);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> buscarTransaccionPorId(@PathVariable Long id) {
        TransaccionResponseDTO transaccion = transaccionService.findTransaccionDTO(id);

        // Si el service devuelve null, lanzamos la excepción para el Handler
        if (transaccion == null) {
            throw new NoSuchElementException("No existe la transacción con Id: " + id);
        } else {
            return ResponseEntity.ok(transaccion);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(@PathVariable Long id, @Valid @RequestBody TransaccionRequestDTO dto) {
        TransaccionResponseDTO actualizada = transaccionService.updateTransaccion(id, dto);

        if (actualizada == null) {
            throw new NoSuchElementException("No se puede actualizar. La transacción con ID " + id + " no existe.");
        } else {
            return ResponseEntity.ok(actualizada);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        // El service ya tiene la lógica de verificar existencia o lanzar excepción
        transaccionService.deleteTransaccion(id);
        // Si no hubo error, retornamos 204 No Content
        return ResponseEntity.noContent().build();
    }
}
