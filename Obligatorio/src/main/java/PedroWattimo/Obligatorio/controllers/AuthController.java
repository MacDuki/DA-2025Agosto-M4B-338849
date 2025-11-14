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
import PedroWattimo.Obligatorio.models.SesionAdmin;
import PedroWattimo.Obligatorio.models.SesionPropietario;
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
        // Login retorna la sesión del dominio
        SesionPropietario sesionDominio = Fachada.getInstancia().loginPropietario(cedula, password);

        // Crear DTO para la respuesta
        PropietarioAutenticadoDTO dto = new PropietarioAutenticadoDTO(
                sesionDominio.getPropietario().getCedula(),
                sesionDominio.getPropietario().getNombreCompleto(),
                sesionDominio.getPropietario().getEstadoActual() != null
                        ? sesionDominio.getPropietario().getEstadoActual().nombre()
                        : "HABILITADO");

        // Almacenar ambos en la sesión HTTP
        session.setAttribute("propietario", dto);
        session.setAttribute("sesionPropietario", sesionDominio);

        Respuesta respuesta = new Respuesta("loginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/propietarios/logout")
    public ResponseEntity<List<Respuesta>> logoutPropietario(HttpSession session) {
        // Recuperar la sesión del dominio
        SesionPropietario sesionDominio = (SesionPropietario) session.getAttribute("sesionPropietario");

        // Si existe, hacer logout en el sistema
        if (sesionDominio != null) {
            Fachada.getInstancia().logoutPropietario(sesionDominio);
        }

        // Invalidar sesión HTTP
        try {
            session.invalidate();
        } catch (IllegalStateException ignore) {
        }

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
        // Login retorna la sesión del dominio
        SesionAdmin sesionDominio = Fachada.getInstancia().loginAdmin(cedula, password);

        // Crear DTO para la respuesta
        AdminAutenticadoDto dto = new AdminAutenticadoDto(
                sesionDominio.getAdministrador().getCedula(),
                sesionDominio.getAdministrador().getNombreCompleto());

        // Almacenar ambos en la sesión HTTP
        session.setAttribute("admin", dto);
        session.setAttribute("sesionAdmin", sesionDominio);

        Respuesta respuesta = new Respuesta("adminLoginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/admin/logout")
    public ResponseEntity<List<Respuesta>> logoutAdmin(
            HttpSession session,
            @RequestParam(name = "cedula", required = false) Integer cedula) throws OblException {

        // Recuperar la sesión del dominio
        SesionAdmin sesionDominio = (SesionAdmin) session.getAttribute("sesionAdmin");

        // Si existe, hacer logout en el sistema
        if (sesionDominio != null) {
            Fachada.getInstancia().logoutAdmin(sesionDominio);
        }

        // Invalidar la sesión HTTP
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
