package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;
import observador.Observable;
import observador.Observador;

/**
 * Controlador para el caso de uso: Dashboard del Propietario.
 * 
 */
@RestController
@RequestMapping("/propietarios/dashboard")
@Scope("session")
public class DashboardPropietarioController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public DashboardPropietarioController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;

        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {

        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "dashboard_actualizado");
        notificacion.put("mensaje", "Hay cambios en tu dashboard");

        Respuesta respuesta = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    /**
     * 
     * Obtiene los datos del dashboard del propietario autenticado.
     */
    @PostMapping
    public ResponseEntity<List<Respuesta>> obtenerDashboard(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        PropietarioDashboardDto dto = fachada.dashboardDePropietario(propietario.getCedula());

        Respuesta respuesta = new Respuesta("dashboard", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    /**
     * 
     * Borra las notificaciones del propietario autenticado.
     */
    @PostMapping("/notificaciones/borrar")
    public ResponseEntity<List<Respuesta>> borrarNotificaciones(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int borradas = fachada.borrarNotificacionesDePropietario(propietario.getCedula());

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("borradas", borradas);
        resultado.put("mensaje", borradas == 0
                ? "No hay notificaciones para borrar"
                : "Se borraron " + borradas + " notificaciones");

        Respuesta respuesta = new Respuesta("notificacionesBorradas", resultado);

        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "notificaciones_borradas");
        notificacion.put("mensaje", "Notificaciones borradas");
        Respuesta notifSSE = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(notifSSE));

        return ResponseEntity.ok(List.of(respuesta));
    }
}
