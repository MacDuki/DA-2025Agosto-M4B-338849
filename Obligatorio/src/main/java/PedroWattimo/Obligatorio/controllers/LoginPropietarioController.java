package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.models.subsistemas.Fachada;
import PedroWattimo.Obligatorio.models.sesiones.SesionPropietario;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/propietarios/login")
public class LoginPropietarioController {

    private final Fachada fachada = Fachada.getInstancia();

    @PostMapping
    public List<Respuesta> loginPropietario(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {

        SesionPropietario sesionDominio = fachada.loginPropietario(cedula, password);
        PropietarioAutenticadoDTO dto = new PropietarioAutenticadoDTO(sesionDominio);

        session.setAttribute("propietario", dto);
        session.setAttribute("sesionPropietario", sesionDominio);

        Respuesta respuesta = new Respuesta("loginExitoso", dto);
        return List.of(respuesta);
    }
}
