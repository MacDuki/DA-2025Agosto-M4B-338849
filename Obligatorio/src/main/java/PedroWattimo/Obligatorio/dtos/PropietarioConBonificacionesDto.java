package PedroWattimo.Obligatorio.dtos;

import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Propietario;

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

    public PropietarioConBonificacionesDto(Propietario p) {
        this.nombreCompleto = p.getNombreCompleto();
        this.estado = p.getEstadoActual() != null ? p.getEstadoActual().nombre() : "HABILITADO";
        this.bonificaciones = BonificacionAsignadaDto.desdeLista(p.getAsignaciones());
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
