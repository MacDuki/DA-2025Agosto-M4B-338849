package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.Fachada;

@RestController
@RequestMapping("/propietarios")
public class PropietariosController {

    @GetMapping("/{cedula}/dashboard")
    public ResponseEntity<PropietarioDashboardDto> dashboard(@PathVariable int cedula) {
        PropietarioDashboardDto dto = Fachada.getInstancia().dashboardDePropietario(cedula);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{cedula}/dashboard/version")
    public ResponseEntity<Map<String, Object>> version(@PathVariable int cedula) {
        long version = Fachada.getInstancia().versionDashboardDePropietario(cedula);
        Map<String, Object> body = new HashMap<>();
        body.put("version", version);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{cedula}/notificaciones")
    public ResponseEntity<Map<String, Object>> borrarNotificaciones(@PathVariable int cedula) {
        int borradas = Fachada.getInstancia().borrarNotificacionesDePropietario(cedula);
        Map<String, Object> body = new HashMap<>();
        body.put("borradas", borradas);
        if (borradas == 0) {
            body.put("mensaje", "No hay notificaciones para borrar");
        }
        return ResponseEntity.ok(body);
    }
}
