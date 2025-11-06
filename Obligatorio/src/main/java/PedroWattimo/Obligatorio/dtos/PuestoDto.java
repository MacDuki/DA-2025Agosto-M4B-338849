package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para representar un puesto en listas.
 */
public class PuestoDto {
    private Long id;
    private String nombre;
    private String direccion;

    public PuestoDto() {
    }

    public PuestoDto(Long id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
