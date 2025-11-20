package PedroWattimo.Obligatorio.dtos;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Estado;

public class EstadoDto {
    private String nombre;

    public EstadoDto() {
    }

    public EstadoDto(String nombre) {
        this.nombre = nombre;
    }

    public EstadoDto(Estado e) {
        this.nombre = e.nombre();
    }

    public static List<EstadoDto> desdeLista(List<Estado> lista) {
        List<EstadoDto> ret = new ArrayList<>();
        for (Estado e : lista) {
            ret.add(new EstadoDto(e));
        }
        return ret;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
