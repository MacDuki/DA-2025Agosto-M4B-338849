package PedroWattimo.Obligatorio.models.exceptions;

public class UsuarioDeshabilitadoException extends RuntimeException {
    public UsuarioDeshabilitadoException() {
        super("Usuario deshabilitado, no puede ingresar al sistema");
    }
    public UsuarioDeshabilitadoException(String message) {
        super(message);
    }
}
