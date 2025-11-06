package PedroWattimo.Obligatorio.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.EmularTransitoRequest;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

/**
 * Controlador REST para operaciones administrativas.
 * Sin lógica de negocio: solo coordina request/response y delega en Fachada.
 */
@RestController
@RequestMapping("/admin")

public class AdminController {

    private final Fachada fachada = Fachada.getInstancia();

    /**
     * POST /admin/emular-transito
     * Emula un tránsito de un vehículo por un puesto.
     * Request: {puestoId, matricula, fechaHora}
     * Respuesta: EmularTransitoResultado con todos los detalles del tránsito
     */
    @PostMapping("/emular-transito")
    public ResponseEntity<Respuesta> emularTransito(@RequestBody EmularTransitoRequest request) {
        try {
            // Validar request (sin lógica de negocio, solo validación de formato)
            if (request.getPuestoId() == null) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "El ID del puesto es requerido"));
            }
            if (request.getMatricula() == null || request.getMatricula().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "La matrícula es requerida"));
            }
            if (request.getFechaHora() == null || request.getFechaHora().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "La fecha/hora es requerida"));
            }

            // Parsear fecha/hora (sin lógica, solo conversión)
            LocalDateTime fechaHora;
            try {
                fechaHora = LocalDateTime.parse(request.getFechaHora(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "Formato de fecha/hora inválido. Use ISO-8601"));
            }

            // Delegar en Fachada (una sola línea de lógica)
            EmularTransitoResultado resultado = fachada.emularTransito(
                    request.getPuestoId(),
                    request.getMatricula(),
                    fechaHora);

            return ResponseEntity.ok(new Respuesta("ok", resultado));

        } catch (TarifaNoDefinidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Respuesta("error", "Error inesperado: " + e.getMessage()));
        }
    }
}
