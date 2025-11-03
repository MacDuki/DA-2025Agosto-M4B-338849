package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaPuestos {
    private List<Puesto> puestos = new ArrayList<>();

    protected SistemaPuestos() {
    }

    public List<Puesto> getPuestos() {
        return puestos;
    }

    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto.getTablaTarifas();
    }

}
