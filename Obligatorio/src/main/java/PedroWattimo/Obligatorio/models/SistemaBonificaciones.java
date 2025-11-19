package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaBonificaciones {

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

    public Optional<Bonificacion> bonificacionVigente(Propietario p, Puesto puesto) {
        if (p == null || puesto == null)
            return Optional.empty();

        Optional<AsignacionBonificacion> asignacion = p.bonificacionAsignadaPara(puesto);
        return asignacion.map(AsignacionBonificacion::bonificacion);
    }

    public List<Bonificacion> listarBonificaciones() {
        return List.copyOf(bonificaciones);
    }

    public Bonificacion buscarPorNombre(String nombre) throws OblException {
        Bonificacion bonificacion = FabricaBonificaciones.crear(nombre);

        boolean existe = bonificaciones.stream()
                .anyMatch(b -> nombre.equalsIgnoreCase(b.getNombre()));

        if (!existe) {
            throw new OblException("Bonificación no encontrada: " + nombre);
        }

        return bonificacion;
    }

    public void asignarBonificacionAPropietario(Propietario propietario, Bonificacion bonificacion, Puesto puesto)
            throws OblException {
        propietario.validarAsignacionBonificacion(bonificacion, puesto);
        propietario.asignarBonificacion(bonificacion, puesto);
        Fachada.getInstancia().avisar(Eventos.BONIFICACION_ASIGNADA);
    }

    public void asignarBonificacion(String cedula, String nombreBonificacion, String nombrePuesto)
            throws OblException {
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedula);
        Bonificacion bonificacion = buscarPorNombre(nombreBonificacion);
        Puesto puesto = Fachada.getInstancia().buscarPuestoPorNombreInterno(nombrePuesto);
        asignarBonificacionAPropietario(propietario, bonificacion, puesto);
    }

    public void agregarBonificacion(Bonificacion bonificacion) throws OblException {
        if (bonificacion == null) {
            throw new OblException("La bonificación no puede ser nula");
        }

        String nombre = bonificacion.getNombre();
        for (Bonificacion b : bonificaciones) {
            if (nombre.equalsIgnoreCase(b.getNombre())) {
                throw new OblException("Ya existe una bonificación con el nombre: " + nombre);
            }
        }

        bonificaciones.add(bonificacion);
    }
}
