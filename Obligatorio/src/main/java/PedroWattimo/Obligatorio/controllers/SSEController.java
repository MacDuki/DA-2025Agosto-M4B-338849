package PedroWattimo.Obligatorio.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import PedroWattimo.Obligatorio.models.ConexionNavegador;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador SSE (Server-Sent Events) para notificaciones en tiempo real.
 * Utiliza ConexionNavegador con scope de sesión para mantener una única
 * conexión SSE por sesión de usuario.
 */
@RestController
@RequestMapping("/sse")
@Scope("session")
public class SSEController {

    private final ConexionNavegador conexionNavegador;

    public SSEController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    /**
     * Endpoint SSE único para conectar el navegador del cliente.
     * La conexión SSE se mantiene por sesión y puede ser usada desde cualquier
     * controlador.
     */
    @GetMapping(value = "/conectar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conectar(HttpSession session) {
        // Inicializar la conexión SSE para esta sesión
        conexionNavegador.conectarSSE();

        // Enviar mensaje de bienvenida
        conexionNavegador.enviarMensaje("Conexión SSE establecida");

        return conexionNavegador.getConexionSSE();
    }
}
