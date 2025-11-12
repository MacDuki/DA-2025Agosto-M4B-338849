package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // ---------------- Propietarios ----------------
    @PostMapping("/propietarios/login")
    public ResponseEntity<List<Respuesta>> loginPropietario(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {
        PropietarioAutenticadoDTO dto = Fachada.getInstancia().loginPropietario(cedula, password);
        session.setAttribute("propietario", dto);
        Respuesta respuesta = new Respuesta("loginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/propietarios/logout")
    public ResponseEntity<List<Respuesta>> logoutPropietario(HttpSession session) {
        session.invalidate();
        Map<String, String> resultado = new HashMap<>();
        resultado.put("mensaje", "Sesión cerrada exitosamente");
        Respuesta respuesta = new Respuesta("logoutExitoso", resultado);
        return ResponseEntity.ok(List.of(respuesta));
    }

    // ---------------- Administradores ----------------
    @PostMapping("/admin/login")
    public ResponseEntity<List<Respuesta>> logiAdmin(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {
        AdminAutenticadoDto dto = Fachada.getInstancia().loginAdmin(cedula, password);
        session.setAttribute("admin", dto);
        Respuesta respuesta = new Respuesta("adminLoginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/admin/logout")
    public ResponseEntity<List<Respuesta>> logoutAdmin(
            HttpSession session,
            @RequestParam(name = "cedula", required = false) Integer cedula) throws OblException {
        boolean hizoLogout = false;

        Object adminObj = session.getAttribute("admin");
        if (adminObj instanceof AdminAutenticadoDto dto) {
            Fachada.getInstancia().logoutAdmin(dto.getCedula());
            hizoLogout = true;
        } else if (cedula != null) {
            // Permitir desloguear por cédula si no hay sesión asociada (p. ej., Postman o
            // sesión expirada)
            Fachada.getInstancia().logoutAdmin(cedula);
            hizoLogout = true;
        }

        // Si no se pudo identificar ninguna sesión/admin, informar explícitamente
        if (!hizoLogout) {
            throw new OblException("No hay sesión activa");
        }

        // Invalidar la sesión HTTP si existía
        try {
            session.invalidate();
        } catch (IllegalStateException ignore) {
        }

        Map<String, String> resultado = new HashMap<>();
        resultado.put("mensaje", "Sesión cerrada exitosamente");
        Respuesta respuesta = new Respuesta("logoutExitoso", resultado);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
