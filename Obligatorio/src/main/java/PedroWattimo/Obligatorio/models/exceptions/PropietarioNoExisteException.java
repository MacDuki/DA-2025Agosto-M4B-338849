package PedroWattimo.Obligatorio.models.exceptions;

public class PropietarioNoExisteException extends RuntimeException {
    public PropietarioNoExisteException() {
        super("El propietario no existe");
    }

    public PropietarioNoExisteException(String message) {
        super(message);
    }
}
