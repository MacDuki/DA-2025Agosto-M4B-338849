package PedroWattimo.Obligatorio.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.EmularTransitoRequest;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.dtos.TarifaDto;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;
import observador.Observable;
import observador.Observador;

/**
 * Controlador para el caso de uso: Emular Tránsito.
 */
@RestController
@RequestMapping("/admin/emular-transito")
@Scope("session")
public class EmularTransitoController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public EmularTransitoController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;

        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {

        System.out.println("[EmularTransitoController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "evento_sistema",
                "Cambio en el sistema detectado");

        Respuesta respuesta = new Respuesta("sistema_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    /**
     * 
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
     * 
     * Obtiene las tarifas de un puesto específico.
     */
    @GetMapping("/puestos/{id}/tarifas")
    public ResponseEntity<List<TarifaDto>> obtenerTarifasPuesto(@PathVariable Long id) {
        try {

            List<TarifaDto> dtos = fachada.obtenerTarifasDePuesto(id);

            return ResponseEntity.ok(dtos);
        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 
     * Emula un tránsito de un vehículo por un puesto.
     */
    @PostMapping
    public ResponseEntity<Respuesta> emularTransito(@RequestBody EmularTransitoRequest request) {
        try {
            LocalDateTime fechaHora = LocalDateTime.parse(request.getFechaHora(), DateTimeFormatter.ISO_DATE_TIME);

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
