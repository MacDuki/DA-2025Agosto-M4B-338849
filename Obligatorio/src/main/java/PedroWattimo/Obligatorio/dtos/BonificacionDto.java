package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para transferir información de una bonificación.
 */
public class BonificacionDto {
    private String nombre;
    private double porcentaje;

    public BonificacionDto() {
    }

    public BonificacionDto(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
