package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.SesionPropietario;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador para el caso de uso: Logout de Propietario.
 * 
 */
@RestController
@RequestMapping("/propietarios/logout")
public class LogoutPropietarioController {

    private final Fachada fachada = Fachada.getInstancia();

    /**
     * 
     * Cierra la sesión del propietario autenticado.
     */
    @PostMapping
    public ResponseEntity<List<Respuesta>> logoutPropietario(HttpSession session) {

        SesionPropietario sesionDominio = (SesionPropietario) session.getAttribute("sesionPropietario");

        if (sesionDominio != null) {
            fachada.logoutPropietario(sesionDominio);
        }

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
