package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;

public class NotificacionDto {
    private LocalDateTime fechaHora;
    private String mensaje;

    public NotificacionDto() {
    }

    public NotificacionDto(LocalDateTime fechaHora, String mensaje) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
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
