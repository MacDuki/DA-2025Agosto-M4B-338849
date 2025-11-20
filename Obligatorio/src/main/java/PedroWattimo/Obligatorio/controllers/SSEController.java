package PedroWattimo.Obligatorio.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import PedroWattimo.Obligatorio.models.entidades.ConexionNavegador;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/sse")
@Scope("session")
public class SSEController {

    private final ConexionNavegador conexionNavegador;

    public SSEController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/conectar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conectar(HttpSession session) {
        // Inicializar la conexión SSE para esta sesión
        conexionNavegador.conectarSSE();

        // Enviar mensaje de bienvenida
        conexionNavegador.enviarMensaje("Conexión SSE establecida");

        return conexionNavegador.getConexionSSE();
    }
}
