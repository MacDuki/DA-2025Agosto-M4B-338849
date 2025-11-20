package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.LogoutResultadoDto;
import PedroWattimo.Obligatorio.models.sesiones.SesionPropietario;
import PedroWattimo.Obligatorio.models.subsistemas.Fachada;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/propietarios/logout")
public class LogoutPropietarioController {

    private final Fachada fachada = Fachada.getInstancia();

    @PostMapping
    public List<Respuesta> logoutPropietario(HttpSession session) {

        SesionPropietario sesionDominio = (SesionPropietario) session.getAttribute("sesionPropietario");

        if (sesionDominio != null) {
            fachada.logoutPropietario(sesionDominio);
        }

        try {
            session.invalidate();
        } catch (IllegalStateException ignore) {

        }

        LogoutResultadoDto resultado = new LogoutResultadoDto("Sesión cerrada exitosamente");
        Respuesta respuesta = new Respuesta("logoutExitoso", resultado);
        return Respuesta.lista(respuesta);
    }
}
