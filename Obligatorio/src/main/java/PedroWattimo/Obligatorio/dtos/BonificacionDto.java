package PedroWattimo.Obligatorio.dtos;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.Bonificacion;

public class BonificacionDto {
    private String nombre;
    private double porcentaje;

    public BonificacionDto() {
    }

    public BonificacionDto(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    public BonificacionDto(Bonificacion b) {
        this.nombre = b.getNombre();
        this.porcentaje = b.getPorcentaje();
    }

    public static List<BonificacionDto> desdeLista(List<Bonificacion> lista) {
        List<BonificacionDto> ret = new ArrayList<>();
        for (Bonificacion b : lista) {
            ret.add(new BonificacionDto(b));
        }
        return ret;
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
