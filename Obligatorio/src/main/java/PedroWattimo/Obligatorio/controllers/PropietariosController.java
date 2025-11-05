package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/propietarios")
public class PropietariosController {

    @PostMapping("/dashboard")
    public ResponseEntity<List<Respuesta>> dashboard(HttpSession session) throws OblException {
        PropietarioDTO propietario = (PropietarioDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = Integer.parseInt(propietario.getCedula());
        PropietarioDashboardDto dto = Fachada.getInstancia().dashboardDePropietario(cedula);
        Respuesta respuesta = new Respuesta("dashboard", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/dashboard/version")
    public ResponseEntity<List<Respuesta>> version(HttpSession session) throws OblException {
        PropietarioDTO propietario = (PropietarioDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int cedula = Integer.parseInt(propietario.getCedula());
        long version = Fachada.getInstancia().versionDashboardDePropietario(cedula);
        Map<String, Object> versionData = new HashMap<>();
        versionData.put("version", version);
        Respuesta respuesta = new Respuesta("version", versionData);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/notificaciones/borrar")
    public ResponseEntity<List<Respuesta>> borrarNotificaciones(HttpSession session) throws OblException {
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
        Respuesta respuesta = new Respuesta("notificacionesBorradas", resultado);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
