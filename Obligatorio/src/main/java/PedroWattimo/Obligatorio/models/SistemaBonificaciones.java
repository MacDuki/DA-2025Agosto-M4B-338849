package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SistemaBonificaciones {
    private List<Bonificacion> bonificaciones = new ArrayList<Bonificacion>();
    private List<AsignacionBonificacion> asignaciones = new ArrayList<AsignacionBonificacion>();

    protected SistemaBonificaciones() {
    }

    // Encapsulamiento: retornar copias inmutables para prevenir modificaciones
    // externas
    public List<Bonificacion> getBonificaciones() {
        return List.copyOf(bonificaciones);
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return List.copyOf(asignaciones);
    }

    // Métodos internos para acceso directo (package-private para uso de SeedData)
    List<Bonificacion> obtenerBonificacionesInternas() {
        return bonificaciones;
    }

    List<AsignacionBonificacion> obtenerAsignacionesInternas() {
        return asignaciones;
    }

    /**
     * Busca la bonificación vigente asignada a un propietario para un puesto
     * específico.
     * Delega en el Propietario la búsqueda de su asignación.
     */
    public Optional<Bonificacion> bonificacionVigente(Propietario p, Puesto puesto) {
        if (p == null || puesto == null)
            return Optional.empty();

        Optional<AsignacionBonificacion> asignacion = p.bonificacionAsignadaPara(puesto);
        return asignacion.map(AsignacionBonificacion::bonificacion);
    }
}
