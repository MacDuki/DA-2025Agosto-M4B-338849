package PedroWattimo.Obligatorio.controllers;

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
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.models.Estado;
import PedroWattimo.Obligatorio.models.FabricaEstados;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Propietario;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * Controlador REST para el caso de uso: Cambiar estado de propietario.
 * Sin lógica de negocio: solo coordina request/response y delega en Fachada.
 */
@RestController
@RequestMapping("/estados")
public class EstadosController {

    private final Fachada fachada = Fachada.getInstancia();

    /**
     * GET /estados/buscar-propietario?cedula=XXX
     * Busca un propietario por cédula y devuelve sus datos básicos.
     */
    @GetMapping("/buscar-propietario")
    public ResponseEntity<Respuesta> buscarPropietario(@RequestParam String cedula) {
        try {
            Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);
            PropietarioResumenDto dto = new PropietarioResumenDto(
                    propietario.getNombreCompleto(),
                    propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                            : FabricaEstados.crearHabilitado().nombre(),
                    propietario.getSaldoActual());
            return ResponseEntity.ok(new Respuesta("ok", dto));
        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Respuesta("error", e.getMessage()));
        }
    }

    /**
     * GET /estados/listar
     * Lista todos los estados disponibles.
     */
    @GetMapping("/listar")
    public ResponseEntity<Respuesta> listarEstados() {
        try {
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
     * POST /estados/cambiar-estado
     * Cambia el estado de un propietario.
     * Request: {cedula, nuevoEstado}
     */
    @PostMapping("/cambiar-estado")
    public ResponseEntity<Respuesta> cambiarEstado(@RequestBody CambiarEstadoRequest request) {
        try {
            if (request.getCedula() == null || request.getCedula().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Respuesta("error", "La cédula es obligatoria"));
            }
            if (request.getNuevoEstado() == null || request.getNuevoEstado().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Respuesta("error", "El nuevo estado es obligatorio"));
            }

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

    // DTO interno para estado
    public static class EstadoDto {
        private String nombre;

        public EstadoDto() {
        }

        public EstadoDto(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    // DTO para la request de cambiar estado
    public static class CambiarEstadoRequest {
        private String cedula;
        private String nuevoEstado;

        public CambiarEstadoRequest() {
        }

        public String getCedula() {
            return cedula;
        }

        public void setCedula(String cedula) {
            this.cedula = cedula;
        }

        public String getNuevoEstado() {
            return nuevoEstado;
        }

        public void setNuevoEstado(String nuevoEstado) {
            this.nuevoEstado = nuevoEstado;
        }
    }
}
