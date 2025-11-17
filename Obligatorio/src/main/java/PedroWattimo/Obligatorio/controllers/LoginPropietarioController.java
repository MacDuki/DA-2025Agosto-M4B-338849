package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.SesionPropietario;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador para el caso de uso: Login de Propietario.
 * Patrón MVC: orquesta la autenticación delegando en Fachada.
 */
@RestController
@RequestMapping("/propietarios/login")
public class LoginPropietarioController {

    private final Fachada fachada = Fachada.getInstancia();

    /**
     * 
     * Autentica un propietario con cédula y contraseña.
     */
    @PostMapping
    public ResponseEntity<List<Respuesta>> loginPropietario(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {

        SesionPropietario sesionDominio = fachada.loginPropietario(cedula, password);

        PropietarioAutenticadoDTO dto = new PropietarioAutenticadoDTO(
                sesionDominio.getPropietario().getCedula(),
                sesionDominio.getPropietario().getNombreCompleto(),
                sesionDominio.getPropietario().getEstadoActual() != null
                        ? sesionDominio.getPropietario().getEstadoActual().nombre()
                        : "HABILITADO");

        session.setAttribute("propietario", dto);
        session.setAttribute("sesionPropietario", sesionDominio);

        Respuesta respuesta = new Respuesta("loginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
