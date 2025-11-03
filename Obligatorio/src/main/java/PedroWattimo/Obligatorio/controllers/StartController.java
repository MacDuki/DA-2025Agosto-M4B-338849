package PedroWattimo.Obligatorio.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/start")
public class StartController {

    @GetMapping("/saludar")
    public String saludar() {
        return "¡Hola Mundo con Spring Boot!";
    }
}
