package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.Notificacion;

public class NotificacionDto {
    private LocalDateTime fechaHora;
    private String mensaje;

    public NotificacionDto() {
    }

    public NotificacionDto(LocalDateTime fechaHora, String mensaje) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
    }

    public NotificacionDto(Notificacion n) {
        this.fechaHora = n.getFechaHora();
        this.mensaje = n.getMensaje();
    }

    public static List<NotificacionDto> desdeLista(List<Notificacion> lista) {
        List<NotificacionDto> ret = new ArrayList<>();
        for (Notificacion n : lista) {
            ret.add(new NotificacionDto(n));
        }
        return ret;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
