package PedroWattimo.Obligatorio.models.events;

import java.time.Instant;

public class DashboardActualizadoEvent {
    private final String cedula;
    private final Instant when = Instant.now();

    public DashboardActualizadoEvent(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public Instant getWhen() {
        return when;
    }
}
