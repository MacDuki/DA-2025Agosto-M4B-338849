package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.dtos.RespuestaVista;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/propietarios")
public class PropietariosController {

    @PostMapping("/dashboard")
    public ResponseEntity<List<RespuestaVista>> dashboard(HttpSession session) throws OblException {
        PropietarioDTO propietario = (PropietarioDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = Integer.parseInt(propietario.getCedula());
        PropietarioDashboardDto dto = Fachada.getInstancia().dashboardDePropietario(cedula);
        RespuestaVista respuesta = new RespuestaVista("dashboard", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/dashboard/version")
    public ResponseEntity<List<RespuestaVista>> version(HttpSession session) throws OblException {
        PropietarioDTO propietario = (PropietarioDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = Integer.parseInt(propietario.getCedula());
        long version = Fachada.getInstancia().versionDashboardDePropietario(cedula);
        Map<String, Object> versionData = new HashMap<>();
        versionData.put("version", version);
        RespuestaVista respuesta = new RespuestaVista("version", versionData);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/notificaciones/borrar")
    public ResponseEntity<List<RespuestaVista>> borrarNotificaciones(HttpSession session) throws OblException {
        PropietarioDTO propietario = (PropietarioDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = Integer.parseInt(propietario.getCedula());
        int borradas = Fachada.getInstancia().borrarNotificacionesDePropietario(cedula);
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("borradas", borradas);
        if (borradas == 0) {
            resultado.put("mensaje", "No hay notificaciones para borrar");
        } else {
            resultado.put("mensaje", "Se borraron " + borradas + " notificaciones");
        }
        RespuestaVista respuesta = new RespuestaVista("notificacionesBorradas", resultado);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
