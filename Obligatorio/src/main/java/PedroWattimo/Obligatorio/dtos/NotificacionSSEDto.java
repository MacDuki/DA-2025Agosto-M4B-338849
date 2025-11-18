package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para notificaciones enviadas por SSE (Server-Sent Events).
 */
public class NotificacionSSEDto {
    private String tipo;
    private String mensaje;

    public NotificacionSSEDto() {
    }

    public NotificacionSSEDto(String tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
