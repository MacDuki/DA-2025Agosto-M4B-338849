package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.AsignacionBonificacion;

public class BonificacionAsignadaDto {
    private String nombre;
    private String puesto;
    private LocalDateTime fechaAsignada;

    public BonificacionAsignadaDto() {
    }

    public BonificacionAsignadaDto(String nombre, String puesto, LocalDateTime fechaAsignada) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.fechaAsignada = fechaAsignada;
    }

    public BonificacionAsignadaDto(AsignacionBonificacion ab) {
        this.nombre = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
        this.puesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
        this.fechaAsignada = ab.getFechaHora();
    }

    public static List<BonificacionAsignadaDto> desdeLista(List<AsignacionBonificacion> lista) {
        List<BonificacionAsignadaDto> ret = new ArrayList<>();
        for (AsignacionBonificacion ab : lista) {
            ret.add(new BonificacionAsignadaDto(ab));
        }
        return ret;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public LocalDateTime getFechaAsignada() {
        return fechaAsignada;
    }

    public void setFechaAsignada(LocalDateTime fechaAsignada) {
        this.fechaAsignada = fechaAsignada;
    }
}
