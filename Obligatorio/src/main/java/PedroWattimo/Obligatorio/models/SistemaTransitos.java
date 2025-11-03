package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaTransitos {
    private List<Transito> transitos = new ArrayList<Transito>();

    protected SistemaTransitos() {
    }

    public List<Transito> getTransitos() {
        return transitos;
    }

    // Este metodo debe retornar transito
    public void emularTransito(Puesto puesto, String matriculaVehiculo, LocalDateTime fechaHora) {
        // Lógica para emular un tránsito (simulación)
        return;
    }
}
