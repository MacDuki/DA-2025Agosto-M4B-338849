package PedroWattimo.Obligatorio.dtos;

public class EstadoDto {
    private String nombre;

    public EstadoDto() {
    }

    public EstadoDto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
