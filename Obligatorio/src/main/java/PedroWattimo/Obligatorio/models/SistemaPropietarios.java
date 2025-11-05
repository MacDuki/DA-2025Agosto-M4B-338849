package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaPropietarios {
    private List<Propietario> propietarios = new ArrayList<Propietario>();

    protected SistemaPropietarios() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Propietario> getPropietarios() {
        return List.copyOf(propietarios);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Propietario> obtenerPropietariosInternos() {
        return propietarios;
    }

    /** Busca un propietario por su cédula (texto exacto). */
    public Propietario buscarPorCedula(String cedula) {
        if (cedula == null)
            return null;
        for (Propietario p : propietarios) {
            if (cedula.equals(p.getCedula())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Repositorio compuesto para el CU: retorna el propietario con sus colecciones
     * ya precargadas
     * (vehículos, tránsitos, bonificaciones, notificaciones). En memoria ya están
     * embebidas.
     */
    public Propietario findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(int cedula) {
        return buscarPorCedula(String.valueOf(cedula));
    }
}
