package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaAdministradores {
    private List<Administrador> administradores = new ArrayList<>();

    protected SistemaAdministradores() {
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }
}
