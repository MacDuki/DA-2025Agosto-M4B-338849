package PedroWattimo.Obligatorio.models.entidades;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * Patrón Strategy: cada tipo de bonificación implementa su propia \
 * lógica de descuento.
 */
public abstract class Bonificacion {
    protected String nombre;
    protected double porcentaje;

    protected Bonificacion() {
    }

    protected Bonificacion(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    public static void validarNoNula(Bonificacion bonificacion) throws OblException {
        if (bonificacion == null) {
            throw new OblException("La bonificación no puede ser nula");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public abstract double calcularDescuento(Propietario prop, Vehiculo veh, Puesto puesto,
            Tarifa tarifa, LocalDateTime fh,
            List<Transito> transitosPrevios);
}
