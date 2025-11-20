package PedroWattimo.Obligatorio.models.fabricas;

import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.BonificacionExonerados;
import PedroWattimo.Obligatorio.models.entidades.BonificacionFrecuentes;
import PedroWattimo.Obligatorio.models.entidades.BonificacionTrabajadores;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * 
 * Patrón Factory Method: centraliza la creación de bonificaciones por nombre.
 */
public class FabricaBonificaciones {

    public static Bonificacion crear(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la bonificación no puede estar vacío");
        }

        String nombreNormalizado = nombre.trim();

        if ("Exonerados".equalsIgnoreCase(nombreNormalizado)) {
            return new BonificacionExonerados();
        } else if ("Frecuentes".equalsIgnoreCase(nombreNormalizado)) {
            return new BonificacionFrecuentes();
        } else if ("Trabajadores".equalsIgnoreCase(nombreNormalizado)) {
            return new BonificacionTrabajadores();
        } else {
            throw new OblException("Tipo de bonificación desconocido: " + nombre);
        }
    }

    public static Bonificacion crearExonerados() {
        return new BonificacionExonerados();
    }

    public static Bonificacion crearFrecuentes() {
        return new BonificacionFrecuentes();
    }

    public static Bonificacion crearTrabajadores() {
        return new BonificacionTrabajadores();
    }
}
