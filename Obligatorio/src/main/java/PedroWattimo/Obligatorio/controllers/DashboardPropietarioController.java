package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.NotificacionesBorradasDto;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.entidades.ConexionNavegador;
import PedroWattimo.Obligatorio.models.subsistemas.Fachada;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/propietarios/dashboard")
@Scope("session")
public class DashboardPropietarioController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public DashboardPropietarioController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @PostMapping("/vistaConectada")
    public void vistaConectada() {
        fachada.agregarObservador(this);
    }

    @PostMapping("/vistaCerrada")
    public void vistaCerrada() {
        fachada.eliminarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "dashboard_actualizado",
                "Hay cambios en tu dashboard");

        Respuesta respuesta = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    @PostMapping
    public List<Respuesta> obtenerDashboard(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        Propietario p = fachada.buscarPropietarioPorCedula(propietario.getCedula());
        if (p == null) {
            throw new OblException("El propietario no existe");
        }

        PropietarioDashboardDto dto = new PropietarioDashboardDto(p);
        dto.setVersion(fachada.versionDashboardDePropietario(propietario.getCedula()));

        Respuesta respuesta = new Respuesta("dashboard", dto);
        return List.of(respuesta);
    }

    @PostMapping("/notificaciones/borrar")
    public List<Respuesta> borrarNotificaciones(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int borradas = fachada.borrarNotificacionesDePropietario(propietario.getCedula());

        NotificacionesBorradasDto resultado = new NotificacionesBorradasDto(
                borradas,
                borradas == 0
                        ? "No hay notificaciones para borrar"
                        : "Se borraron " + borradas + " notificaciones");

        Respuesta respuesta = new Respuesta("notificacionesBorradas", resultado);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "notificaciones_borradas",
                "Notificaciones borradas");
        Respuesta notifSSE = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(notifSSE));

        return List.of(respuesta);
    }
}
