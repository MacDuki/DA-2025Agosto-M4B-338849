package PedroWattimo.Obligatorio.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class PropietarioDomainTests {

    @Test
    void notificaciones_orden_descendente() {
        Propietario p = new Propietario("1", "Test", "pwd");
        p.getNotificaciones().add(new Notificacion(LocalDateTime.now().minusDays(1), "A", p));
        p.getNotificaciones().add(new Notificacion(LocalDateTime.now().minusDays(3), "B", p));
        p.getNotificaciones().add(new Notificacion(LocalDateTime.now(), "C", p));

        var ordenadas = p.notificacionesOrdenadasDesc();
        assertEquals("C", ordenadas.get(0).getMensaje());
        assertEquals("A", ordenadas.get(1).getMensaje());
        assertEquals("B", ordenadas.get(2).getMensaje());
    }

    @Test
    void agregados_por_vehiculo() {
        Propietario p = new Propietario("1", "Test", "pwd");
        Categoria cat = new Categoria("Automóvil");
        Vehiculo v = new Vehiculo("ABC123", "Modelo X", "Rojo", cat, p);
        p.getVehiculos().add(v);
        Puesto puesto = new Puesto("Peaje 1", "Dir");
        v.getTransitos().add(new Transito(LocalDateTime.now().minusDays(1), puesto, v, 100, 120, "Ninguna", 0));
        v.getTransitos().add(new Transito(LocalDateTime.now(), puesto, v, 100, 120, "Promo", 10));

        assertEquals(2, p.cantidadTransitosDe(v));
        assertEquals(120 + (120 - (120 * 0.10)), p.totalGastadoPor(v), 0.0001);
    }

    @Test
    void borrar_notificaciones_devuelve_cantidad() {
        Propietario p = new Propietario("1", "Test", "pwd");
        p.getNotificaciones().add(new Notificacion(LocalDateTime.now(), "A", p));
        p.getNotificaciones().add(new Notificacion(LocalDateTime.now(), "B", p));
        int cant = p.borrarNotificaciones();
        assertEquals(2, cant);
        assertTrue(p.getNotificaciones().isEmpty());
    }
}
