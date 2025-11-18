package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.context.annotation.Scope;
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
import PedroWattimo.Obligatorio.dtos.BonificacionDto;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/admin/asignar-bonificacion")
@Scope("session")
public class AsignarBonificacionesController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public AsignarBonificacionesController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        System.out.println("[AsignarBonificacionesController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "evento_sistema",
                "Cambio en el sistema detectado");

        Respuesta respuesta = new Respuesta("sistema_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    /**
     * Lista todas las bonificaciones definidas en el sistema.
     */
    @GetMapping("/bonificaciones")
    public ResponseEntity<List<BonificacionDto>> listarBonificaciones() {
        try {

            List<BonificacionDto> dtos = fachada.listarBonificacionesDto();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Lista todos los puestos registrados en el sistema.
     */
    @GetMapping("/puestos")
    public ResponseEntity<List<PuestoDto>> listarPuestos() {
        try {

            List<PuestoDto> dtos = fachada.listarPuestosDto();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
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

            PropietarioConBonificacionesDto dto = fachada.obtenerPropietarioConBonificaciones(cedula);

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
     * Asigna una bonificación a un propietario para un puesto específico.
     */
    @PostMapping
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
