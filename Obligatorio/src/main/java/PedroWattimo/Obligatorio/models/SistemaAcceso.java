package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaAcceso {

    private ArrayList<SesionPropietario> sesionesPropietarios = new ArrayList<>();
    private ArrayList<SesionAdmin> sesionesAdmin = new ArrayList<>();

    private Fachada fachada;

    protected SistemaAcceso() {

    }

    void setFachada(Fachada fachada) {
        this.fachada = fachada;
    }

    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        Propietario prop = fachada.autenticarYValidarPropietarioInterno(cedula, password);

        SesionPropietario s = new SesionPropietario(LocalDateTime.now(), prop);
        sesionesPropietarios.add(s);
        return s;
    }

    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
        Administrador admin = fachada.autenticarYValidarAdminInterno(cedula, password);

        SesionAdmin s = new SesionAdmin(LocalDateTime.now(), admin);
        sesionesAdmin.add(s);
        return s;
    }

    public void logoutPropietario(SesionPropietario sesion) {
        sesionesPropietarios.remove(sesion);
    }

    public void logoutAdmin(SesionAdmin sesion) {
        fachada.desloguearAdminInterno(sesion.getAdministrador());
        sesionesAdmin.remove(sesion);
    }

}
