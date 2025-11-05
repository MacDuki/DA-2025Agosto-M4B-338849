package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaEstados {
    private List<Estado> estados = new ArrayList<Estado>();

    protected SistemaEstados() {

    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas

    public List<Estado> getEstados() {
        return List.copyOf(estados);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Estado> obtenerEstadosInternos() {
        return estados;
    }
}
