package PedroWattimo.Obligatorio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
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

    @GetMapping("/estados")
    public Respuesta listarEstados() {
        List<Estado> estados = fachada.listarEstados();
        List<EstadoDto> estadoDtos = new ArrayList<>();
        for (Estado estado : estados) {
            estadoDtos.add(new EstadoDto(estado.nombre()));
        }
        return new Respuesta("ok", estadoDtos);
    }

    @GetMapping("/propietario")
    public Respuesta buscarPropietario(@RequestParam String cedula) throws OblException {
        Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);
        PropietarioResumenDto dto = new PropietarioResumenDto(
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre() : "HABILITADO",
                propietario.getSaldoActual());
        return new Respuesta("ok", dto);
    }

    @PostMapping
    public Respuesta cambiarEstado(@RequestBody CambiarEstadoRequest request) throws OblException {
        fachada.cambiarEstadoPropietario(request.getCedula(), request.getNuevoEstado());
        return new Respuesta("ok", "Estado cambiado exitosamente");
    }
}
