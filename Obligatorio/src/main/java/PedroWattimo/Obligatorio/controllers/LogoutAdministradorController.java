package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.LogoutResultadoDto;
import PedroWattimo.Obligatorio.models.subsistemas.Fachada;
import PedroWattimo.Obligatorio.models.sesiones.SesionAdmin;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/logout")
public class LogoutAdministradorController {

    private final Fachada fachada = Fachada.getInstancia();

    @PostMapping
    public List<Respuesta> logoutAdmin(HttpSession session) {

        SesionAdmin sesionDominio = (SesionAdmin) session.getAttribute("sesionAdmin");

        if (sesionDominio != null) {
            fachada.logoutAdmin(sesionDominio);
        }

        try {
            session.invalidate();
        } catch (IllegalStateException ignore) {

        }

        LogoutResultadoDto resultado = new LogoutResultadoDto("Sesión cerrada exitosamente");
        Respuesta respuesta = new Respuesta("logoutExitoso", resultado);
        return List.of(respuesta);
    }
}
