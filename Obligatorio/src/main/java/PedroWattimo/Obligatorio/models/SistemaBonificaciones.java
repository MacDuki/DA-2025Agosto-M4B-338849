package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import PedroWattimo.Obligatorio.models.exceptions.OblException;
import observador.Observable;

public class SistemaBonificaciones extends Observable {

    public enum Eventos {
        BONIFICACION_ASIGNADA
    }

    private List<Bonificacion> bonificaciones = new ArrayList<Bonificacion>();
    private List<AsignacionBonificacion> asignaciones = new ArrayList<AsignacionBonificacion>();

    protected SistemaBonificaciones() {
    }

    public List<Bonificacion> getBonificaciones() {
        return List.copyOf(bonificaciones);
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return List.copyOf(asignaciones);
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
     * Busca una bonificación por nombre usando la fábrica.
     */
    public Bonificacion buscarPorNombre(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la bonificación no puede estar vacío");
        }

        // Usar la fábrica para crear/obtener la bonificación
        Bonificacion bonificacion = FabricaBonificaciones.crear(nombre);

        // Verificar que existe en el sistema
        boolean existe = bonificaciones.stream()
                .anyMatch(b -> nombre.equalsIgnoreCase(b.getNombre()));

        if (!existe) {
            throw new OblException("Bonificación no encontrada: " + nombre);
        }

        return bonificacion;
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

        // Notifica a las vistas que se asignó una nueva bonificación
        avisar(Eventos.BONIFICACION_ASIGNADA);
    }

    // Inyección de dependencias para resolver objetos
    private SistemaPropietariosYAdmin sistemaPropietarios;
    private SistemaPuestosYTarifas sistemaPuestos;

    public void setSistemaPropietarios(SistemaPropietariosYAdmin sistema) {
        this.sistemaPropietarios = sistema;
    }

    public void setSistemaPuestos(SistemaPuestosYTarifas sistema) {
        this.sistemaPuestos = sistema;
    }

    /**
     * Asigna una bonificación resolviendo los objetos por sus identificadores.
     * Orquesta la resolución de objetos y delega la asignación.
     */
    public void asignarBonificacion(String cedula, String nombreBonificacion, String nombrePuesto)
            throws OblException {
        Propietario propietario = sistemaPropietarios.buscarPorCedula(cedula);
        Bonificacion bonificacion = buscarPorNombre(nombreBonificacion);
        Puesto puesto = sistemaPuestos.obtenerPorNombre(nombrePuesto);
        asignarBonificacionAPropietario(propietario, bonificacion, puesto);
    }

    /**
     * Lista todas las bonificaciones como DTOs.
     * Patrón Experto: el sistema que conoce las bonificaciones transforma a DTO.
     */
    public List<PedroWattimo.Obligatorio.dtos.BonificacionDto> listarBonificacionesDto() {
        List<PedroWattimo.Obligatorio.dtos.BonificacionDto> dtos = new ArrayList<>();
        for (Bonificacion b : bonificaciones) {
            dtos.add(new PedroWattimo.Obligatorio.dtos.BonificacionDto(b.getNombre(), b.getPorcentaje()));
        }
        return dtos;
    }

    // ---- Casos de uso: Alta de entidades ----

    /**
     * Agrega una nueva bonificación al sistema.
     * Valida que no exista una bonificación con el mismo nombre.
     */
    public void agregarBonificacion(Bonificacion bonificacion) throws OblException {
        if (bonificacion == null) {
            throw new OblException("La bonificación no puede ser nula");
        }

        String nombre = bonificacion.getNombre();
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la bonificación no puede estar vacío");
        }

        // Validar que no exista
        for (Bonificacion b : bonificaciones) {
            if (nombre.equalsIgnoreCase(b.getNombre())) {
                throw new OblException("Ya existe una bonificación con el nombre: " + nombre);
            }
        }

        bonificaciones.add(bonificacion);
    }
}
