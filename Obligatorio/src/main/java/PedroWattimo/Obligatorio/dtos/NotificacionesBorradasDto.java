package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para el resultado de borrar notificaciones.
 */
public class NotificacionesBorradasDto {
    private int borradas;
    private String mensaje;

    public NotificacionesBorradasDto() {
    }

    public NotificacionesBorradasDto(int borradas, String mensaje) {
        this.borradas = borradas;
        this.mensaje = mensaje;
    }

    public int getBorradas() {
        return borradas;
    }

    public void setBorradas(int borradas) {
        this.borradas = borradas;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
