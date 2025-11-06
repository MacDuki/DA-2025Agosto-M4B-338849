package PedroWattimo.Obligatorio.models.exceptions;

/**
 * Excepción lanzada cuando no existe una tarifa definida para una categoría en
 * un puesto específico.
 */
public class TarifaNoDefinidaException extends OblException {
    public TarifaNoDefinidaException(String message) {
        super(message);
    }
}
