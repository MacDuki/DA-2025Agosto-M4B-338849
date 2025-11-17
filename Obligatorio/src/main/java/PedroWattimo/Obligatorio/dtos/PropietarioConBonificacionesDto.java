package PedroWattimo.Obligatorio.dtos;

import java.util.List;

public class PropietarioConBonificacionesDto {
    private String nombreCompleto;
    private String estado;
    private List<BonificacionAsignadaDto> bonificaciones;

    public PropietarioConBonificacionesDto() {
    }

    public PropietarioConBonificacionesDto(String nombreCompleto, String estado,
            List<BonificacionAsignadaDto> bonificaciones) {
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.bonificaciones = bonificaciones;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<BonificacionAsignadaDto> getBonificaciones() {
        return bonificaciones;
    }

    public void setBonificaciones(List<BonificacionAsignadaDto> bonificaciones) {
        this.bonificaciones = bonificaciones;
    }
}
