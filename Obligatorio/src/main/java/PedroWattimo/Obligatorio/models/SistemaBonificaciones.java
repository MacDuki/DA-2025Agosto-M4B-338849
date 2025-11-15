package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

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

    /**
     * Lista todas las bonificaciones definidas en el sistema.
     */
    public List<Bonificacion> listarBonificaciones() {
        return List.copyOf(bonificaciones);
    }

    /**
     * Busca una bonificación por nombre.
     */
    public Bonificacion buscarPorNombre(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la bonificación no puede estar vacío");
        }
        return bonificaciones.stream()
                .filter(b -> nombre.equalsIgnoreCase(b.getNombre()))
                .findFirst()
                .orElseThrow(() -> new OblException("Bonificación no encontrada: " + nombre));
    }

    /**
     * Asigna una bonificación a un propietario para un puesto específico.
     * Valida todas las reglas de negocio.
     */
    public void asignarBonificacionAPropietario(Propietario propietario, Bonificacion bonificacion, Puesto puesto)
            throws OblException {
        // Validaciones obligatorias
        if (bonificacion == null) {
            throw new OblException("Debe especificar una bonificación");
        }
        if (puesto == null) {
            throw new OblException("Debe especificar un puesto");
        }
        if (propietario == null) {
            throw new OblException("no existe el propietario");
        }

        // Validar estado del propietario
        if (propietario.getEstadoActual() != null && !propietario.getEstadoActual().permiteIngresar()) {
            throw new OblException("El propietario esta deshabilitado. No se pueden asignar bonificaciones");
        }

        // Validar que no tenga bonificación ya asignada para ese puesto
        if (propietario.tieneBonificacionPara(puesto)) {
            throw new OblException("Ya tiene una bonificación asignada para ese puesto");
        }

        // Delegar al propietario la asignación (Patrón Experto)
        propietario.asignarBonificacion(bonificacion, puesto);
    }
}
