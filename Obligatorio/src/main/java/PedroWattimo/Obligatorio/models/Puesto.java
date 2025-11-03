package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class Puesto {
    private String nombre;
    private String direccion;
    private List<Tarifa> tablaTarifas;
    private List<Transito> transitos;
    private List<AsignacionBonificacion> asignaciones;

    public Puesto() {
        this.tablaTarifas = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
    }

    public Puesto(String nombre, String direccion) {
        this();
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Tarifa> getTablaTarifas() {
        return tablaTarifas;
    }

    public List<Transito> getTransitos() {
        return transitos;
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones;
    }

}
