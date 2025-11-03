package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaVehiculos {
    private List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();

    protected SistemaVehiculos() {

    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

}
