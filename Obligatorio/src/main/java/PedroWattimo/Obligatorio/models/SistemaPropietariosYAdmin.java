package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.NotificacionDto;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.dtos.TransitoDto;
import PedroWattimo.Obligatorio.dtos.VehiculoResumenDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import observador.Observable;

public class SistemaPropietariosYAdmin extends Observable {

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

    // -------- Accesos Encapsulados --------
    public List<Propietario> getPropietarios() {
        return List.copyOf(propietarios);
    }

    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    public Propietario buscarPorCedula(int cedula) {
        for (Propietario p : propietarios) {
            if (cedula == p.getCedula()) {
                return p;
            }
        }
        return null;
    }

    public Propietario buscarPorCedula(String cedula) throws OblException {
        if (cedula == null || cedula.isBlank()) {
            throw new OblException("La cédula no puede estar vacía");
        }
        try {
            int cedulaInt = Integer.parseInt(cedula);
            Propietario prop = buscarPorCedula(cedulaInt);
            if (prop == null) {
                throw new OblException("no existe el propietario");
            }
            return prop;
        } catch (NumberFormatException e) {
            throw new OblException("Cédula inválida");
        }
    }

    public Propietario findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(int cedula) {
        return buscarPorCedula(cedula);
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
        // Incrementar versión del dashboard e notificar cambios
        dashboardVersion.merge(propietario.getCedula(), 1L, Long::sum);

        avisar(Eventos.NOTIFICACION_REGISTRADA);
    }

    public Propietario autenticarPropietario(int cedula, String password) throws OblException {
        if (password == null || password.isBlank()) {
            throw new OblException("Acceso denegado");
        }

        Propietario dueño = buscarPorCedula(cedula);
        if (dueño == null) {
            throw new OblException("Acceso denegado");
        }

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
        if (password == null || password.isBlank()) {
            throw new OblException("Acceso denegado");
        }

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

    public PropietarioDashboardDto dashboardDePropietario(int cedula) throws OblException {
        Propietario p = findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");

        PropietarioDashboardDto dto = new PropietarioDashboardDto();
        dto.setPropietario(new PropietarioResumenDto(p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : FabricaEstados.crearHabilitado().nombre(),
                p.getSaldoActual()));

        List<BonificacionAsignadaDto> bonos = new ArrayList<>();
        for (AsignacionBonificacion ab : p.bonificacionesAsignadas()) {
            String nombre = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
            String puesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
            bonos.add(new BonificacionAsignadaDto(nombre, puesto, ab.getFechaHora()));
        }
        dto.setBonificaciones(bonos);

        List<VehiculoResumenDto> vehs = new ArrayList<>();
        for (Vehiculo v : p.vehiculos()) {
            vehs.add(new VehiculoResumenDto(
                    v.getMatricula(), v.getModelo(), v.getColor(),
                    p.cantidadTransitosDe(v), p.totalGastadoPor(v)));
        }
        dto.setVehiculos(vehs);

        List<Transito> trans = new ArrayList<>(p.transitosOrdenadosDesc());
        trans.sort(Comparator.comparing(Transito::fechaHora).reversed());
        List<TransitoDto> transDtos = new ArrayList<>();
        for (Transito t : trans) {
            transDtos.add(new TransitoDto(
                    t.puesto() != null ? t.puesto().getNombre() : null,
                    t.vehiculo() != null ? t.vehiculo().getMatricula() : null,
                    t.categoriaVehiculo(),
                    t.costoConTarifa(),
                    t.nombreBonificacion(),
                    t.montoBonificacion(),
                    t.totalPagado(),
                    t.fechaHora()));
        }
        dto.setTransitos(transDtos);

        List<Notificacion> notifs = new ArrayList<>(p.notificacionesOrdenadasDesc());
        notifs.sort(Comparator.comparing(Notificacion::getFechaHora).reversed());
        List<NotificacionDto> notifDtos = new ArrayList<>();
        for (Notificacion n : notifs) {
            notifDtos.add(new NotificacionDto(n.getFechaHora(), n.getMensaje()));
        }
        dto.setNotificaciones(notifDtos);

        long ver = dashboardVersion.getOrDefault(p.getCedula(), 0L);
        dto.setVersion(ver);
        return dto;
    }

    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        Propietario p = findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");
        int borradas = p.borrarNotificaciones();
        dashboardVersion.merge(p.getCedula(), 1L, Long::sum);

        avisar(Eventos.NOTIFICACIONES_BORRADAS);
        return borradas;
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

        avisar(Eventos.CAMBIO_ESTADO);
    }

    private SistemaEstados sistemaEstados;

    public void setSistemaEstados(SistemaEstados sistema) {
        this.sistemaEstados = sistema;
    }

    public void cambiarEstadoPropietario(String cedulaPropietario, String nombreNuevoEstado) throws OblException {
        Propietario propietario = buscarPorCedula(cedulaPropietario);
        Estado nuevoEstado = sistemaEstados.buscarPorNombre(nombreNuevoEstado);
        cambiarEstadoDePropietario(propietario, nuevoEstado);
    }

    public PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto obtenerPropietarioConBonificaciones(
            String cedula) throws OblException {
        Propietario propietario = buscarPorCedula(cedula);

        List<BonificacionAsignadaDto> bonificaciones = new ArrayList<>();
        for (AsignacionBonificacion ab : propietario.getAsignaciones()) {
            String nombreBonif = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
            String nombrePuesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
            bonificaciones.add(new BonificacionAsignadaDto(nombreBonif, nombrePuesto, ab.getFechaHora()));
        }

        return new PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto(
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                        : FabricaEstados.crearHabilitado().nombre(),
                bonificaciones);
    }

    public PropietarioResumenDto buscarPropietarioResumenDto(String cedula) throws OblException {
        Propietario propietario = buscarPorCedula(cedula);
        return new PropietarioResumenDto(
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                        : FabricaEstados.crearHabilitado().nombre(),
                propietario.getSaldoActual());
    }

    public Administrador agregarAdministrador(int cedula, String nombreCompleto, String password) throws OblException {
        if (cedula <= 0) {
            throw new OblException("La cédula debe ser mayor a 0");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new OblException("El nombre completo no puede estar vacío");
        }
        if (password == null || password.isBlank()) {
            throw new OblException("La contraseña no puede estar vacía");
        }

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
        if (cedula <= 0) {
            throw new OblException("La cédula debe ser mayor a 0");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new OblException("El nombre completo no puede estar vacío");
        }
        if (password == null || password.isBlank()) {
            throw new OblException("La contraseña no puede estar vacía");
        }

        Propietario existente = buscarPorCedula(cedula);
        if (existente != null) {
            throw new OblException("Ya existe un propietario con la cédula: " + cedula);
        }

        Propietario nuevoPropietario = new Propietario(cedula, nombreCompleto, password);
        propietarios.add(nuevoPropietario);
        return nuevoPropietario;
    }
}
