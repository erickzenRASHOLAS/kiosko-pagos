package cl.kiosko.ms_pagos.Controller;

import cl.kiosko.ms_pagos.DTO.*;
import cl.kiosko.ms_pagos.Service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/transacciones")
@Tag(name="Transaccion", description = "Operaciones relacionadas con las transacciones")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;
    //@Valid sirve para que se cumplan los @ que puse anteriormente (ahorra crear if)


    @PostMapping("")
    @Operation(summary = "Crear transacción", description = "Se crea/guarda una nueva Transacción (debe tener UN metodo de pago si o si)")
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Valid @RequestBody TransaccionRequestDTO dto) {
        // El service se encarga de la lógica de guardado y vinculación
        TransaccionResponseDTO nuevaTransaccion = transaccionService.saveTransaccion(dto);
        // Retornamos el DTO con estado 201 Created
        return new ResponseEntity<>(nuevaTransaccion, HttpStatus.CREATED);
    }

    @GetMapping("")
    @Operation(summary="Lisar Transacciones", description = "Busca y muestra todas las transacciones existentes")
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
    @Operation(summary="Buscar Transacción por ID", description = "Se busca una transacción según su ID(se debe poner en la URL) y la muestra (si es que existe)")
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
    @Operation(summary="Actualizar Transacción", description = "Se pone el ID de la transacción a actualizar y si es que existe cambia los antiguos parametros por los nuevos que deben enviarse en formato JSON")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(@PathVariable Long id, @Valid @RequestBody TransaccionRequestDTO dto) {
        TransaccionResponseDTO actualizada = transaccionService.updateTransaccion(id, dto);

        if (actualizada == null) {
            throw new NoSuchElementException("No se puede actualizar. La transacción con ID " + id + " no existe.");
        } else {
            return ResponseEntity.ok(actualizada);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar Transacción por ID", description = "Busca una transacción según su ID (debe ponerse en la URL) y si la encuentra la elimina")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        // El service ya tiene la lógica de verificar existencia o lanzar excepción
        transaccionService.deleteTransaccion(id);
        // Si no hubo error, retornamos 204 No Content
        return ResponseEntity.noContent().build();
    }
}
