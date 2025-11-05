package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaNotificaciones {
    private List<Notificacion> notificaciones = new ArrayList<Notificacion>();

    protected SistemaNotificaciones() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Notificacion> getNotificaciones() {
        return List.copyOf(notificaciones);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Notificacion> obtenerNotificacionesInternas() {
        return notificaciones;
    }
}
