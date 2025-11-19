package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.SesionAdmin;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/login")
public class LoginAdminController {

    private final Fachada fachada = Fachada.getInstancia();

    @PostMapping
    public List<Respuesta> loginAdmin(
            @RequestParam int cedula,
            @RequestParam String password,
            HttpSession session) throws OblException {

        SesionAdmin sesionDominio = fachada.loginAdmin(cedula, password);

        AdminAutenticadoDto dto = new AdminAutenticadoDto(
                sesionDominio.getAdministrador().getCedula(),
                sesionDominio.getAdministrador().getNombreCompleto());

        session.setAttribute("admin", dto);
        session.setAttribute("sesionAdmin", sesionDominio);

        Respuesta respuesta = new Respuesta("adminLoginExitoso", dto);
        return List.of(respuesta);
    }
}
