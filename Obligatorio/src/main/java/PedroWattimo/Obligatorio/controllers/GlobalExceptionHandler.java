package PedroWattimo.Obligatorio.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import PedroWattimo.Obligatorio.models.exceptions.CredencialesInvalidasException;
import PedroWattimo.Obligatorio.models.exceptions.PropietarioNoExisteException;
import PedroWattimo.Obligatorio.models.exceptions.SinNotificacionesException;
import PedroWattimo.Obligatorio.models.exceptions.UsuarioDeshabilitadoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<String> handleCredenciales(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioDeshabilitadoException.class)
    public ResponseEntity<String> handleDeshabilitado(UsuarioDeshabilitadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(PropietarioNoExisteException.class)
    public ResponseEntity<String> handlePropNoExiste(PropietarioNoExisteException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SinNotificacionesException.class)
    public ResponseEntity<String> handleSinNotificaciones(SinNotificacionesException ex) {
        // Por diseño del CU, este caso puede devolverse como 200 con mensaje; dejamos
        // 200
        return ResponseEntity.ok(ex.getMessage());
    }
}
