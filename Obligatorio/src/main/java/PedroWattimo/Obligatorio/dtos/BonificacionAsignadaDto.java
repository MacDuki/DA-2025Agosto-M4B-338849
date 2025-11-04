package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;

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
