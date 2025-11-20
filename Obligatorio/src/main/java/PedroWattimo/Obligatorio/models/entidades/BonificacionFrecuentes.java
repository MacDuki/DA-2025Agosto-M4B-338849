package PedroWattimo.Obligatorio.models.entidades;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bonificación para usuarios frecuentes.
 * Regla: 50% de descuento a partir del segundo tránsito del mismo vehículo
 * en el mismo día y mismo puesto.
 */
public class BonificacionFrecuentes extends Bonificacion {

    public BonificacionFrecuentes() {
        super("Frecuentes", 50.0);
    }

    @Override
    public double calcularDescuento(Propietario prop, Vehiculo veh, Puesto puesto,
            Tarifa tarifa, LocalDateTime fh,
            List<Transito> transitosPrevios) {
        if (tarifa == null) {
            return 0.0;
        }

        // Si ya hay al menos un tránsito previo en el día, aplicar 50% descuento
        if (transitosPrevios != null && !transitosPrevios.isEmpty()) {
            return tarifa.getMonto() * 0.5;
        }

        return 0.0;
    }
}
