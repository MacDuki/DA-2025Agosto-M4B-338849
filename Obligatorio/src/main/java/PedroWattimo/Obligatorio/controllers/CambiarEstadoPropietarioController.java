package PedroWattimo.Obligatorio.controllers;

import java.util.ArrayList;
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
import PedroWattimo.Obligatorio.dtos.CambiarEstadoRequest;
import PedroWattimo.Obligatorio.dtos.EstadoDto;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Estado;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Propietario;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/admin/cambiar-estado")
@Scope("session")
public class CambiarEstadoPropietarioController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public CambiarEstadoPropietarioController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        System.out.println("[CambiarEstadoPropietarioController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "estado_propietario_cambiado",
                "Estado de propietario actualizado");

        Respuesta respuesta = new Respuesta("estado_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    /**
     * Lista todos los estados disponibles.
     */
    @GetMapping("/estados")
    public ResponseEntity<Respuesta> listarEstados() {
        try {
            // Convertir objetos de dominio a DTOs
            List<Estado> estados = fachada.listarEstados();
            List<EstadoDto> estadoDtos = new ArrayList<>();
            for (Estado estado : estados) {
                estadoDtos.add(new EstadoDto(estado.nombre()));
            }
            return ResponseEntity.ok(new Respuesta("ok", estadoDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Respuesta("error", "Error al listar estados"));
        }
    }

    /**
     * Busca un propietario por cédula y devuelve sus datos básicos.
     */
    @GetMapping("/propietario")
    public ResponseEntity<Respuesta> buscarPropietario(@RequestParam String cedula) {
        try {
            // Obtener propietario del dominio y convertir a DTO
            Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);
            PropietarioResumenDto dto = new PropietarioResumenDto(
                    propietario.getNombreCompleto(),
                    propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre() : "HABILITADO",
                    propietario.getSaldoActual());
            return ResponseEntity.ok(new Respuesta("ok", dto));
        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        }
    }

    /**
     * Cambia el estado de un propietario.
     */
    @PostMapping
    public ResponseEntity<Respuesta> cambiarEstado(@RequestBody CambiarEstadoRequest request) {
        try {
            fachada.cambiarEstadoPropietario(request.getCedula(), request.getNuevoEstado());
            return ResponseEntity.ok(new Respuesta("ok", "Estado cambiado exitosamente"));

        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Respuesta("error", "Error al cambiar el estado"));
        }
    }
}
