package PedroWattimo.Obligatorio.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.AsignarBonificacionRequest;
import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.BonificacionDto;
import PedroWattimo.Obligatorio.dtos.EmularTransitoRequest;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.models.AsignacionBonificacion;
import PedroWattimo.Obligatorio.models.Bonificacion;
import PedroWattimo.Obligatorio.models.FabricaEstados;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Propietario;
import PedroWattimo.Obligatorio.models.Puesto;
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

    // --------------------------------------------------------------
    // CU: Asignar bonificación a propietario
    // --------------------------------------------------------------

    /**
     * GET /admin/bonificaciones
     * Lista todas las bonificaciones definidas en el sistema.
     */
    @GetMapping("/bonificaciones")
    public ResponseEntity<List<BonificacionDto>> listarBonificaciones() {
        try {
            List<Bonificacion> bonificaciones = fachada.listarBonificaciones();
            List<BonificacionDto> dtos = new ArrayList<>();
            for (Bonificacion b : bonificaciones) {
                dtos.add(new BonificacionDto(b.getNombre(), b.getPorcentaje()));
            }
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /admin/puestos
     * Lista todos los puestos registrados en el sistema.
     */
    @GetMapping("/puestos")
    public ResponseEntity<List<PuestoDto>> listarPuestos() {
        try {
            List<Puesto> puestos = fachada.listarPuestos();
            List<PuestoDto> dtos = new ArrayList<>();
            for (int i = 0; i < puestos.size(); i++) {
                Puesto p = puestos.get(i);
                dtos.add(new PuestoDto((long) i, p.getNombre(), p.getDireccion()));
            }
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /admin/propietario?cedula=xxx
     * Busca un propietario por cédula y devuelve sus datos y bonificaciones
     * asignadas.
     */
    @GetMapping("/propietario")
    public ResponseEntity<Respuesta> buscarPropietario(@RequestParam String cedula) {
        try {
            if (cedula == null || cedula.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "La cédula es requerida"));
            }

            Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);

            // Mapear bonificaciones asignadas
            List<BonificacionAsignadaDto> bonificaciones = new ArrayList<>();
            for (AsignacionBonificacion ab : propietario.getAsignaciones()) {
                String nombreBonif = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
                String nombrePuesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
                bonificaciones.add(new BonificacionAsignadaDto(nombreBonif, nombrePuesto, ab.getFechaHora()));
            }

            PropietarioConBonificacionesDto dto = new PropietarioConBonificacionesDto(
                    propietario.getNombreCompleto(),
                    propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                            : FabricaEstados.crearHabilitado().nombre(),
                    bonificaciones);

            return ResponseEntity.ok(new Respuesta("ok", dto));

        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Respuesta("error", "Error inesperado: " + e.getMessage()));
        }
    }

    /**
     * POST /admin/asignar-bonificacion
     * Asigna una bonificación a un propietario para un puesto específico.
     * Request: {cedula, nombreBonificacion, nombrePuesto}
     */
    @PostMapping("/asignar-bonificacion")
    public ResponseEntity<Respuesta> asignarBonificacion(@RequestBody AsignarBonificacionRequest request) {
        try {
            if (request.getCedula() == null || request.getCedula().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "La cédula es requerida"));
            }
            if (request.getNombreBonificacion() == null || request.getNombreBonificacion().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "Debe especificar una bonificación"));
            }
            if (request.getNombrePuesto() == null || request.getNombrePuesto().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new Respuesta("error", "Debe especificar un puesto"));
            }

            fachada.asignarBonificacion(
                    request.getCedula(),
                    request.getNombreBonificacion(),
                    request.getNombrePuesto());

            return ResponseEntity.ok(new Respuesta("ok", "Bonificación asignada exitosamente"));

        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Respuesta("error", "Error inesperado: " + e.getMessage()));
        }
    }
}
