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

    // Encapsulamiento: retornar copias inmutables para prevenir modificaciones
    // externas
    public List<Tarifa> getTablaTarifas() {
        return tablaTarifas == null ? List.of() : List.copyOf(tablaTarifas);
    }

    public List<Transito> getTransitos() {
        return transitos == null ? List.of() : List.copyOf(transitos);
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones == null ? List.of() : List.copyOf(asignaciones);
    }

    // Patrón Experto: el Puesto conoce sus propias tarifas
    // Método interno para agregar tarifas (uso package-private para SeedData)
    List<Tarifa> obtenerTablaTarifasInterna() {
        return tablaTarifas;
    }

}
