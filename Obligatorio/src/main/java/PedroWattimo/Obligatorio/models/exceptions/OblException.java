package PedroWattimo.Obligatorio.models.exceptions;

/**
 * Excepción genérica del sistema (checked exception).
 * Requiere declaración explícita con 'throws' en los métodos.
 * El mensaje se especifica en cada punto de lanzamiento.
 */
public class OblException extends Exception {
    public OblException(String message) {
        super(message);
    }
}
