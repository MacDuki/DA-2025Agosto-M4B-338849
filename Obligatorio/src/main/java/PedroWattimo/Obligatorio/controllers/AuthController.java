package PedroWattimo.Obligatorio.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.LoginRequest;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.models.Fachada;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<PropietarioDTO> login(@RequestBody LoginRequest request) {
        PropietarioDTO dto = Fachada.getInstancia().loginPropietario(request.getCedula(), request.getPassword());
        return ResponseEntity.ok(dto);
    }
}
