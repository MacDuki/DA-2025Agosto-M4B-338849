package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class Propietario {
    private String cedula;
    private String nombreCompleto;
    private String contraseña;
    private int saldoActual;
    private int saldoMinimoAlerta;
    private Estado estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Transito> transitos;
    private List<AsignacionBonificacion> asignaciones;
    private List<Notificacion> notificaciones;

    public Propietario() {
        this.vehiculos = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public Propietario(String cedula, String nombreCompleto, String contraseña) {
        this();
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.contraseña = contraseña;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getcontraseña() {
        return contraseña;
    }

    public int getSaldoActual() {
        return saldoActual;
    }

    public int getSaldoMinimoAlerta() {
        return saldoMinimoAlerta;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<Transito> getTransitos() {
        return transitos;
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

}
