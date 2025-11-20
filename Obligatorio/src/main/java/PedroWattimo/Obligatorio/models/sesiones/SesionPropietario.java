package PedroWattimo.Obligatorio.models.sesiones;

import java.time.LocalDateTime;

import PedroWattimo.Obligatorio.models.entidades.*;

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
