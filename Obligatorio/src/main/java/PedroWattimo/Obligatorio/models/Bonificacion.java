package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;

/**
 * Clase abstracta que representa una bonificación en el sistema de peajes.
 * Patrón Strategy: cada tipo de bonificación implementa su propia lógica de
 * descuento.
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

    public String getNombre() {
        return nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public abstract double calcularDescuento(Propietario prop, Vehiculo veh, Puesto puesto,
            Tarifa tarifa, LocalDateTime fh,
            SistemaTransitos sistemaTransitos);
}
