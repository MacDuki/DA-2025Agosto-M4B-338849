package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaBonificaciones {
    private List<Bonificacion> bonificaciones = new ArrayList<Bonificacion>();
    private List<AsignacionBonificacion> asignaciones = new ArrayList<AsignacionBonificacion>();

    protected SistemaBonificaciones() {
    }

    public List<Bonificacion> getBonificaciones() {
        return bonificaciones;
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones;
    }
}
