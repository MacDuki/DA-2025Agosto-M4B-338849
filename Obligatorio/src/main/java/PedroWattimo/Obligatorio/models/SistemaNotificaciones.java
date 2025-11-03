package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaNotificaciones {
    private List<Notificacion> notificaciones = new ArrayList<Notificacion>();

    protected SistemaNotificaciones() {
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
}
