package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaAcceso {

    private ArrayList<SesionPropietario> sesionesPropietarios = new ArrayList<>();
    private ArrayList<SesionAdmin> sesionesAdmin = new ArrayList<>();

    private SistemaPropietariosYAdmin sistemaPropietariosYAdmin;

    protected SistemaAcceso() {

    }

    void setSistemaPropietariosYAdmin(SistemaPropietariosYAdmin sistema) {
        this.sistemaPropietariosYAdmin = sistema;
    }

    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        Propietario prop = sistemaPropietariosYAdmin.autenticarYValidarPropietario(cedula, password);

        SesionPropietario s = new SesionPropietario(LocalDateTime.now(), prop);
        sesionesPropietarios.add(s);
        return s;
    }

    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
        Administrador admin = sistemaPropietariosYAdmin.autenticarYValidarAdmin(cedula, password);

        SesionAdmin s = new SesionAdmin(LocalDateTime.now(), admin);
        sesionesAdmin.add(s);
        return s;
    }

    public void logoutPropietario(SesionPropietario sesion) {
        sesionesPropietarios.remove(sesion);
    }

    public void logoutAdmin(SesionAdmin sesion) {
        sistemaPropietariosYAdmin.desloguearAdmin(sesion.getAdministrador());
        sesionesAdmin.remove(sesion);
    }

}
