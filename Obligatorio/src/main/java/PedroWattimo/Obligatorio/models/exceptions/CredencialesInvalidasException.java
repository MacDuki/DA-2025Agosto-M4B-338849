package PedroWattimo.Obligatorio.models.exceptions;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Acceso denegado");
    }
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
