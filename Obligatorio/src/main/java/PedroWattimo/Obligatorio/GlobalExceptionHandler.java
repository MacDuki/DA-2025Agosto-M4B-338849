package PedroWattimo.Obligatorio;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OblException.class)
    public ResponseEntity<List<Respuesta>> handleOblException(OblException ex) {
        String mensaje = ex.getMessage();
        Respuesta respuesta = new Respuesta("error", mensaje);

        if (mensaje.contains("Acceso denegado")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Respuesta.lista(respuesta));
        } else if (mensaje.contains("Ud. Ya está logueado")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Respuesta.lista(respuesta));
        } else if (mensaje.contains("Usuario deshabilitado")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Respuesta.lista(respuesta));
        } else if (mensaje.contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Respuesta.lista(respuesta));
        } else if (mensaje.contains("No hay notificaciones")) {
            return ResponseEntity.ok(Respuesta.lista(respuesta));
        } else if (mensaje.contains("No hay sesión activa")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Respuesta.lista(respuesta));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.lista(respuesta));
    }
}
