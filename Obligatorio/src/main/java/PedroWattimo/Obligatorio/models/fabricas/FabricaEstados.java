package PedroWattimo.Obligatorio.models.fabricas;

import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.entidades.EstadoDeshabilitado;
import PedroWattimo.Obligatorio.models.entidades.EstadoHabilitado;
import PedroWattimo.Obligatorio.models.entidades.EstadoPenalizado;
import PedroWattimo.Obligatorio.models.entidades.EstadoSuspendido;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * 
 * Patrón Factory Method: centraliza la creación de estados por nombre.
 */
public class FabricaEstados {

    private static final Estado HABILITADO = new EstadoHabilitado();
    private static final Estado DESHABILITADO = new EstadoDeshabilitado();
    private static final Estado SUSPENDIDO = new EstadoSuspendido();
    private static final Estado PENALIZADO = new EstadoPenalizado();

    public static Estado crear(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre del estado no puede estar vacío");
        }

        String nombreNormalizado = nombre.trim();

        if ("Habilitado".equalsIgnoreCase(nombreNormalizado)) {
            return HABILITADO;
        } else if ("Deshabilitado".equalsIgnoreCase(nombreNormalizado)) {
            return DESHABILITADO;
        } else if ("Suspendido".equalsIgnoreCase(nombreNormalizado)) {
            return SUSPENDIDO;
        } else if ("Penalizado".equalsIgnoreCase(nombreNormalizado)) {
            return PENALIZADO;
        } else {
            throw new OblException("Tipo de estado desconocido: " + nombre);
        }
    }

    public static Estado crearHabilitado() {
        return HABILITADO;
    }

    public static Estado crearDeshabilitado() {
        return DESHABILITADO;
    }

    public static Estado crearSuspendido() {
        return SUSPENDIDO;
    }

    public static Estado crearPenalizado() {
        return PENALIZADO;
    }
}
