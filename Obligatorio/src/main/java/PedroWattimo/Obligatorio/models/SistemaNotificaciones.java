package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
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

    /**
     * Registra una notificación para un propietario.
     * Delega en el Propietario el registro en su colección personal.
     */
    public void registrar(Propietario p, String mensaje, LocalDateTime fh) {
        if (p == null || mensaje == null || fh == null)
            return;

        // Registrar en el propietario (mantiene la colección embebida)
        p.registrarNotificacion(mensaje, fh);

        // Registrar en el sistema para trazabilidad global
        Notificacion notif = new Notificacion(fh, mensaje, p);
        notificaciones.add(notif);
    }
}
