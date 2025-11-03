package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaPropietarios {
    private List<Propietario> propietarios = new ArrayList<Propietario>();

    protected SistemaPropietarios() {
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }
}
