package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

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

    /**
     * Busca el propietario dueño de un vehículo por su matrícula.
     * Valida que la matrícula sea única por propietario.
     */
    public Propietario propietarioPorMatricula(String matricula) throws OblException {
        if (matricula == null || matricula.isBlank()) {
            throw new OblException("La matrícula no puede estar vacía");
        }

        for (Propietario prop : propietarios) {
            Vehiculo veh = prop.buscarVehiculoPorMatricula(matricula);
            if (veh != null) {
                return prop;
            }
        }

        throw new OblException("No existe el vehículo con matrícula: " + matricula);
    }
}
