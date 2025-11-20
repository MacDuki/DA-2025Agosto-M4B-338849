package PedroWattimo.Obligatorio.dtos;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.Propietario;

public class PropietarioDashboardDto {
    private PropietarioResumenDto propietario;
    private List<BonificacionAsignadaDto> bonificaciones = new ArrayList<>();
    private List<VehiculoResumenDto> vehiculos = new ArrayList<>();
    private List<TransitoDto> transitos = new ArrayList<>();
    private List<NotificacionDto> notificaciones = new ArrayList<>();
    private long version;

    public PropietarioDashboardDto() {
    }

    public PropietarioDashboardDto(Propietario p) {
        this.propietario = new PropietarioResumenDto(p);
        this.bonificaciones = BonificacionAsignadaDto.desdeLista(p.bonificacionesAsignadas());
        this.vehiculos = VehiculoResumenDto.desdeLista(p.vehiculos(), p);
        this.transitos = TransitoDto.desdeLista(p.transitosOrdenadosDesc());
        this.notificaciones = NotificacionDto.desdeLista(p.notificacionesOrdenadasDesc());
    }

    public PropietarioResumenDto getPropietario() {
        return propietario;
    }

    public void setPropietario(PropietarioResumenDto propietario) {
        this.propietario = propietario;
    }

    public List<BonificacionAsignadaDto> getBonificaciones() {
        return bonificaciones;
    }

    public void setBonificaciones(List<BonificacionAsignadaDto> bonificaciones) {
        this.bonificaciones = bonificaciones;
    }

    public List<VehiculoResumenDto> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoResumenDto> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public List<TransitoDto> getTransitos() {
        return transitos;
    }

    public void setTransitos(List<TransitoDto> transitos) {
        this.transitos = transitos;
    }

    public List<NotificacionDto> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<NotificacionDto> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
