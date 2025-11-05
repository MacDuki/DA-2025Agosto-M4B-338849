package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaTransitos {
    private List<Transito> transitos = new ArrayList<Transito>();

    protected SistemaTransitos() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Transito> getTransitos() {
        return List.copyOf(transitos);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Transito> obtenerTransitosInternos() {
        return transitos;
    }

    // Este metodo debe retornar transito
    public void emularTransito(Puesto puesto, String matriculaVehiculo, LocalDateTime fechaHora) {
        // Lógica para emular un tránsito (simulación)
        return;
    }
}
