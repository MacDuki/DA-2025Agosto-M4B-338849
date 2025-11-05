package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaVehiculos {
    private List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();

    protected SistemaVehiculos() {

    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Vehiculo> getVehiculos() {
        return List.copyOf(vehiculos);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Vehiculo> obtenerVehiculosInternos() {
        return vehiculos;
    }

}
