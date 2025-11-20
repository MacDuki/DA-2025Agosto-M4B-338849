package PedroWattimo.Obligatorio.models.sesiones;

import java.time.LocalDateTime;

import PedroWattimo.Obligatorio.models.entidades.*;

public class SesionAdmin {
    private LocalDateTime fechaLogin;
    private Administrador administrador;

    public SesionAdmin(LocalDateTime fechaLogin, Administrador administrador) {
        this.fechaLogin = fechaLogin;
        this.administrador = administrador;
    }

    public LocalDateTime getFechaLogin() {
        return fechaLogin;
    }

    public Administrador getAdministrador() {
        return administrador;
    }
}
