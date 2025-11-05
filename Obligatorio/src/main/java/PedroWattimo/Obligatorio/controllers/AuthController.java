package PedroWattimo.Obligatorio.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.RespuestaVista;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<List<RespuestaVista>> login(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {
        PropietarioDTO dto = Fachada.getInstancia().loginPropietario(cedula, password);

        // Guardar el propietario en la sesión
        session.setAttribute("propietario", dto);

        RespuestaVista respuesta = new RespuestaVista("loginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }

    @PostMapping("/logout")
    public ResponseEntity<List<RespuestaVista>> logout(HttpSession session) {
        // Limpiar la sesión
        session.invalidate();

        Map<String, String> resultado = new HashMap<>();
        resultado.put("mensaje", "Sesión cerrada exitosamente");
        RespuestaVista respuesta = new RespuestaVista("logoutExitoso", resultado);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
