package PedroWattimo.Obligatorio.models.subsistemas;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.AsignacionBonificacion;
import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Puesto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.fabricas.FabricaBonificaciones;

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
        Bonificacion.validarNoNula(bonificacion);

        String nombre = bonificacion.getNombre();
        for (Bonificacion b : bonificaciones) {
            if (nombre.equalsIgnoreCase(b.getNombre())) {
                throw new OblException("Ya existe una bonificación con el nombre: " + nombre);
            }
        }

        bonificaciones.add(bonificacion);
    }
}
