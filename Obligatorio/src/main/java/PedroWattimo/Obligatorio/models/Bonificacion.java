package PedroWattimo.Obligatorio.models;

public class Bonificacion {
    private String nombre;
    private double porcentaje; // opcional

    public Bonificacion() {
    }

    public Bonificacion(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
}
