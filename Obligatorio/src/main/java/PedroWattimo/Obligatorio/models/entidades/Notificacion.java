package PedroWattimo.Obligatorio.models.entidades;

import java.time.LocalDateTime;

public class Notificacion {
    private LocalDateTime fechaHora;
    private String mensaje;
    private Propietario destinatario;

    public Notificacion() {
    }

    public Notificacion(LocalDateTime fechaHora, String mensaje, Propietario destinatario) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
        this.destinatario = destinatario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Propietario getDestinatario() {
        return destinatario;
    }

    public String preview() {
        return "Notificacion{" +
                "fechaHora=" + fechaHora +
                ", mensaje='" + mensaje + '\'' +
                +'}';
    }

    public boolean esDePropietario(Propietario propietario) {
        return this.destinatario.getCedula() == propietario.getCedula();
    }
}
