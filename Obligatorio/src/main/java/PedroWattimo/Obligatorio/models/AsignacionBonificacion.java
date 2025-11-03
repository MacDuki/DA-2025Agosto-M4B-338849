package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;

public class AsignacionBonificacion {

    private LocalDateTime fechaHora;
    private Propietario propietario;
    private Puesto puesto;
    private Bonificacion bonificacion;

    public AsignacionBonificacion() {
    }

    public AsignacionBonificacion(LocalDateTime fechaHora, Propietario propietario, Puesto puesto,
            Bonificacion bonificacion) {
        this.fechaHora = fechaHora;
        this.propietario = propietario;
        this.puesto = puesto;
        this.bonificacion = bonificacion;
    }

    boolean activaPara(Puesto puesto, Propietario propietario) {
        return this.puesto.equals(puesto) && this.propietario.equals(propietario);
    }

    public Bonificacion bonificacion() {
        return bonificacion;
    }

}
