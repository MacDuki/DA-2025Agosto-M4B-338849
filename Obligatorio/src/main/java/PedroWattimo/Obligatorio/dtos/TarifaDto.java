package PedroWattimo.Obligatorio.dtos;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Tarifa;

public class TarifaDto {
    private String categoria;
    private double monto;

    public TarifaDto() {
    }

    public TarifaDto(String categoria, double monto) {
        this.categoria = categoria;
        this.monto = monto;
    }

    public TarifaDto(Tarifa t) {
        this.categoria = t.getCategoria() != null ? t.getCategoria().getNombre() : "Desconocida";
        this.monto = t.getMonto();
    }

    public static List<TarifaDto> desdeLista(List<Tarifa> lista) {
        List<TarifaDto> ret = new ArrayList<>();
        for (Tarifa t : lista) {
            ret.add(new TarifaDto(t));
        }
        return ret;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
