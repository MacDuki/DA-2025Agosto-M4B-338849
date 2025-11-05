package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.RespuestaVista;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<List<RespuestaVista>> login(
            @RequestParam int cedula,
            @RequestParam String password) throws OblException {
        PropietarioDTO dto = Fachada.getInstancia().loginPropietario(cedula, password);
        RespuestaVista respuesta = new RespuestaVista("loginExitoso", dto);
        return ResponseEntity.ok(List.of(respuesta));
    }
}
