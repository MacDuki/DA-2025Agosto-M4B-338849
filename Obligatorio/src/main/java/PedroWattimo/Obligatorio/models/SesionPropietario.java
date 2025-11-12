package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;

public class SesionPropietario {
    private LocalDateTime fechaLogin;
    private Propietario propietario;

    public SesionPropietario(LocalDateTime fechaLogin, Propietario propietario) {
        this.fechaLogin = fechaLogin;
        this.propietario = propietario;
    }

    public LocalDateTime getFechaLogin() {
        return fechaLogin;
    }

    public Propietario getPropietario() {
        return propietario;
    }
}
