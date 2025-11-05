package PedroWattimo.Obligatorio.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OblException.class)
    public ResponseEntity<String> handleOblException(OblException ex) {
        String mensaje = ex.getMessage();

        // Determinar el código HTTP según el mensaje
        if (mensaje.contains("Acceso denegado")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        } else if (mensaje.contains("Usuario deshabilitado")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mensaje);
        } else if (mensaje.contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        } else if (mensaje.contains("No hay notificaciones")) {
            return ResponseEntity.ok(mensaje);
        } else if (mensaje.contains("No hay sesión activa")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        }

        // Por defecto, error 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
    }
}
