package PedroWattimo.Obligatorio.models;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Bonificación para trabajadores.
 * Regla: 80% de descuento si el tránsito ocurre en día hábil (lunes a viernes)
 * en el puesto asignado.
 */
public class BonificacionTrabajadores extends Bonificacion {

    public BonificacionTrabajadores() {
        super("Trabajadores", 80.0);
    }

    @Override
    public double calcularDescuento(Propietario prop, Vehiculo veh, Puesto puesto,
            Tarifa tarifa, LocalDateTime fh,
            SistemaTransitos sistemaTransitos) {
        if (tarifa == null || fh == null) {
            return 0.0;
        }

        // Verificar si es día hábil (lunes a viernes)
        DayOfWeek dia = fh.getDayOfWeek();
        boolean esDiaHabil = dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;

        if (esDiaHabil) {
            return tarifa.getMonto() * 0.8;
        }

        return 0.0;
    }
}
