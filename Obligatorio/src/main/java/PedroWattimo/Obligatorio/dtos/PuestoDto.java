package PedroWattimo.Obligatorio.dtos;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.Puesto;

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

    public PuestoDto(Puesto p, Long id) {
        this.id = id;
        this.nombre = p.getNombre();
        this.direccion = p.getDireccion();
    }

    public static List<PuestoDto> desdeLista(List<Puesto> lista) {
        List<PuestoDto> ret = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            ret.add(new PuestoDto(lista.get(i), (long) i));
        }
        return ret;
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
