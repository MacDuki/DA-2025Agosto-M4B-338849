package PedroWattimo.Obligatorio.models.exceptions;

public class SinNotificacionesException extends RuntimeException {
    public SinNotificacionesException() {
        super("No hay notificaciones para borrar");
    }

    public SinNotificacionesException(String message) {
        super(message);
    }
}
