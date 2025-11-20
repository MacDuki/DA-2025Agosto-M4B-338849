package PedroWattimo.Obligatorio.models.subsistemas;

import java.time.LocalDateTime;
import java.util.ArrayList;

import PedroWattimo.Obligatorio.models.entidades.*;
import PedroWattimo.Obligatorio.models.sesiones.*;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaAcceso {

    private ArrayList<SesionPropietario> sesionesPropietarios = new ArrayList<>();
    private ArrayList<SesionAdmin> sesionesAdmin = new ArrayList<>();

    protected SistemaAcceso() {

    }

    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        Propietario prop = Fachada.getInstancia().autenticarYValidarPropietarioInterno(cedula, password);

        SesionPropietario s = new SesionPropietario(LocalDateTime.now(), prop);
        sesionesPropietarios.add(s);
        return s;
    }

    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
        Administrador admin = Fachada.getInstancia().autenticarYValidarAdminInterno(cedula, password);

        SesionAdmin s = new SesionAdmin(LocalDateTime.now(), admin);
        sesionesAdmin.add(s);
        return s;
    }

    public void logoutPropietario(SesionPropietario sesion) {
        sesionesPropietarios.remove(sesion);
    }

    public void logoutAdmin(SesionAdmin sesion) {
        Fachada.getInstancia().desloguearAdminInterno(sesion.getAdministrador());
        sesionesAdmin.remove(sesion);
    }

}
