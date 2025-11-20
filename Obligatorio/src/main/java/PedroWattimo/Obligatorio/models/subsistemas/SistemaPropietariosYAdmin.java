package PedroWattimo.Obligatorio.models.subsistemas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import PedroWattimo.Obligatorio.models.entidades.Administrador;
import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Vehiculo;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaPropietariosYAdmin {

    public enum Eventos {
        CAMBIO_ESTADO,
        NOTIFICACION_REGISTRADA,
        NOTIFICACIONES_BORRADAS
    }

    protected SistemaPropietariosYAdmin() {
    }

    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();

    private final Map<Integer, Long> dashboardVersion = new ConcurrentHashMap<>();

    public List<Propietario> getPropietarios() {
        return List.copyOf(propietarios);
    }

    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    public Propietario buscarPorCedula(int cedula) throws OblException {
        for (Propietario p : propietarios) {
            if (cedula == p.getCedula()) {
                return p;
            }
        }
        throw new OblException("El propietario no existe");
    }

    public Propietario buscarPorCedula(String cedula) throws OblException {
        if (cedula == null || cedula.isBlank()) {
            throw new OblException("La cédula no puede estar vacía");
        }
        try {
            int cedulaInt = Integer.parseInt(cedula);
            return buscarPorCedula(cedulaInt);
        } catch (NumberFormatException e) {
            throw new OblException("Cédula inválida");
        }
    }

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

    public void registrarNotificacion(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        if (propietario == null || mensaje == null || fechaHora == null)
            return;
        propietario.registrarNotificacion(mensaje, fechaHora);

        dashboardVersion.merge(propietario.getCedula(), 1L, Long::sum);

        Fachada.getInstancia().avisar(Eventos.NOTIFICACION_REGISTRADA);
    }

    public Propietario autenticarPropietario(int cedula, String password) throws OblException {
        Propietario dueño = buscarPorCedula(cedula);
        if (!dueño.passwordCorrecta(password)) {
            throw new OblException("Acceso denegado");
        }
        return dueño;
    }

    public Propietario autenticarYValidarPropietario(int cedula, String password) throws OblException {
        Propietario p = autenticarPropietario(cedula, password);
        if (!p.puedeIngresar()) {
            throw new OblException("Usuario deshabilitado, no puede ingresar al sistema");
        }
        return p;
    }

    public Administrador autenticarYValidarAdmin(int cedula, String password) throws OblException {
        Administrador admin = null;
        for (Administrador a : this.administradores) {
            if (a != null && a.getCedula() == cedula) {
                admin = a;
                break;
            }
        }

        if (admin == null || !admin.passwordCorrecta(password)) {
            throw new OblException("Acceso denegado");
        }

        if (admin.estaLogueado()) {
            throw new OblException("Ud. Ya está logueado");
        }

        admin.loguear();
        return admin;
    }

    public void desloguearAdmin(Administrador admin) {
        if (admin != null) {
            admin.desloguear();
        }
    }

    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        Propietario p = buscarPorCedula(cedula);
        int borradas = p.borrarNotificaciones();
        dashboardVersion.merge(p.getCedula(), 1L, Long::sum);

        Fachada.getInstancia().avisar(Eventos.NOTIFICACIONES_BORRADAS);
        return borradas;
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        Propietario p = buscarPorCedula(cedula);
        return dashboardVersion.getOrDefault(p.getCedula(), 0L);
    }

    public void cambiarEstadoDePropietario(Propietario propietario, Estado nuevoEstado) throws OblException {
        if (propietario == null || nuevoEstado == null) {
            throw new OblException("Propietario y estado no pueden ser nulos");
        }

        Estado estadoActual = propietario.getEstadoActual();

        if (estadoActual != null && estadoActual.equals(nuevoEstado)) {
            throw new OblException("El propietario ya esta en estado " + estadoActual.nombre());
        }

        propietario.cambiarEstado(nuevoEstado);

        LocalDateTime ahora = LocalDateTime.now();
        propietario.registrarNotificacionCambioEstado(nuevoEstado, ahora);

        dashboardVersion.merge(propietario.getCedula(), 1L, Long::sum);

        Fachada.getInstancia().avisar(Eventos.CAMBIO_ESTADO);
    }

    public void cambiarEstadoPropietario(String cedulaPropietario, String nombreNuevoEstado) throws OblException {
        Propietario propietario = buscarPorCedula(cedulaPropietario);
        Estado nuevoEstado = Fachada.getInstancia().buscarEstadoPorNombreInterno(nombreNuevoEstado);
        cambiarEstadoDePropietario(propietario, nuevoEstado);
    }

    public Administrador agregarAdministrador(int cedula, String nombreCompleto, String password) throws OblException {
        Administrador.validarDatosCreacion(cedula, nombreCompleto, password);

        for (Administrador admin : administradores) {
            if (admin.getCedula() == cedula) {
                throw new OblException("Ya existe un administrador con la cédula: " + cedula);
            }
        }

        Administrador nuevoAdmin = new Administrador(cedula, nombreCompleto, password);
        administradores.add(nuevoAdmin);
        return nuevoAdmin;
    }

    public Propietario registrarPropietario(int cedula, String nombreCompleto, String password) throws OblException {
        Propietario.validarDatosCreacion(cedula, nombreCompleto, password);

        try {
            buscarPorCedula(cedula);
            throw new OblException("Ya existe un propietario con la cédula: " + cedula);
        } catch (OblException e) {
            if (e.getMessage().equals("El propietario no existe")) {

            } else {
                throw e;
            }
        }

        Propietario nuevoPropietario = new Propietario(cedula, nombreCompleto, password);
        propietarios.add(nuevoPropietario);
        return nuevoPropietario;
    }
}
