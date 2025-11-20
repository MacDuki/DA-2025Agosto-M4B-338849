package PedroWattimo.Obligatorio.models.entidades;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bonificación para propietarios exonerados.
 * Regla: 100% de descuento en el puesto asignado.
 */
public class BonificacionExonerados extends Bonificacion {

    public BonificacionExonerados() {
        super("Exonerados", 100.0);
    }

    @Override
    public double calcularDescuento(Propietario prop, Vehiculo veh, Puesto puesto,
            Tarifa tarifa, LocalDateTime fh,
            List<Transito> transitosPrevios) {
        // 100% de descuento = toda la tarifa
        return tarifa != null ? tarifa.getMonto() : 0.0;
    }
}
