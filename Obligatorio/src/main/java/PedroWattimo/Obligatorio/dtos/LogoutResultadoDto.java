package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para el resultado de una operación de logout.
 */
public class LogoutResultadoDto {
    private String mensaje;

    public LogoutResultadoDto() {
    }

    public LogoutResultadoDto(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
