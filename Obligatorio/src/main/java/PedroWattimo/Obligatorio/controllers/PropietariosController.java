package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
 * Observador: actualiza el dashboard cuando ocurren cambios en el modelo.
 * Envía notificaciones SSE a través de ConexionNavegador.
 */
@RestController
@RequestMapping("/propietarios")
public class PropietariosController implements Observador {

    @Autowired
    private ConexionNavegador conexionNavegador;

    public PropietariosController() {
        // Suscribirse a los sistemas observables al crear el controlador
        Fachada.getInstancia().registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        // Cuando ocurre un evento, enviar notificación SSE al navegador
        // para que actualice el dashboard
        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "dashboard_actualizado");
        notificacion.put("mensaje", "Hay cambios en tu dashboard");

        // Crear respuesta en formato esperado por vistaWeb.js
        Respuesta respuesta = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    @PostMapping("/dashboard")
    public ResponseEntity<List<Respuesta>> dashboard(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = propietario.getCedula();
        PropietarioDashboardDto dto = Fachada.getInstancia().dashboardDePropietario(cedula);
        Respuesta respuesta = new Respuesta("dashboard", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/notificaciones/borrar")
    public ResponseEntity<List<Respuesta>> borrarNotificaciones(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = propietario.getCedula();
        int borradas = Fachada.getInstancia().borrarNotificacionesDePropietario(cedula);
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("borradas", borradas);
        if (borradas == 0) {
            resultado.put("mensaje", "No hay notificaciones para borrar");
        } else {
            resultado.put("mensaje", "Se borraron " + borradas + " notificaciones");
        }
        Respuesta respuesta = new Respuesta("notificacionesBorradas", resultado);

        // Enviar notificación SSE para actualizar el dashboard automáticamente
        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "notificaciones_borradas");
        notificacion.put("mensaje", "Notificaciones borradas");
        Respuesta notifSSE = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(notifSSE));

        return ResponseEntity.ok(List.of(respuesta));
    }
}
