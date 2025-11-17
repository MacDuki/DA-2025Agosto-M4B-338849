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

/**
 * SistemaPropietariosYAdmin: gestión de propietarios y administradores.
 * Concentra la lógica de gestión de propietarios, notificaciones y dashboards.
 * Observable: notifica cuando cambian los datos de propietarios (estado, saldo,
 * notificaciones, etc.)
 */
public class SistemaPropietariosYAdmin extends Observable {

    /**
     * Enum de eventos que pueden ocurrir en el sistema de propietarios.
     */
    public enum Eventos {
        CAMBIO_ESTADO,
        NOTIFICACION_REGISTRADA,
        NOTIFICACIONES_BORRADAS
    }

    protected SistemaPropietariosYAdmin() {
    }

    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();

    // Historial plano de notificaciones globales .
    private final List<Notificacion> notificacionesGlobales = new ArrayList<>();
    private final Map<Integer, Long> dashboardVersion = new ConcurrentHashMap<>();

    // -------- Accesos Encapsulados --------
    public List<Propietario> getPropietarios() {
        return List.copyOf(propietarios);
    }

    List<Propietario> obtenerPropietariosInternos() {
        return propietarios;
    }

    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    List<Administrador> obtenerAdministradoresInternos() {
        return administradores;
    }

    /** Acceso sólo lectura a trazabilidad de notificaciones globales. */
    public List<Notificacion> getNotificacionesGlobales() {
        return List.copyOf(notificacionesGlobales);
    }

    // -------- Operaciones de Propietarios --------
    /** Busca un propietario por su cédula (Int). */
    public Propietario buscarPorCedula(int cedula) {
        for (Propietario p : propietarios) {
            if (cedula == p.getCedula()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Busca un propietario por cédula (String).
     * Lanza OblException si no existe.
     */
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

    /** Repositorio compuesto en memoria (ya embebido). */
    public Propietario findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(int cedula) {
        return buscarPorCedula(cedula);
    }

    /** Busca propietario dueño de un vehículo por matrícula. */
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

    /**
     * Registrar notificación: delega en Propietario su almacenamiento interno y
     * agrega a trazabilidad global.
     * Notifica a observadores sobre la nueva notificación.
     */
    public void registrarNotificacion(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        if (propietario == null || mensaje == null || fechaHora == null)
            return;
        propietario.registrarNotificacion(mensaje, fechaHora);
        notificacionesGlobales.add(new Notificacion(fechaHora, mensaje, propietario));
        // Incrementar versión del dashboard e notificar cambios
        dashboardVersion.merge(propietario.getCedula(), 1L, Long::sum);
        // Notifica a las vistas que hay una nueva notificación para el propietario
        avisar(Eventos.NOTIFICACION_REGISTRADA);
    }

    // -------- Autenticación de Propietarios --------
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

    // -------- Autenticación de Administradores --------
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

    // -------- Dashboard de Propietario --------
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
        // Notifica a las vistas que se borraron las notificaciones del propietario
        avisar(Eventos.NOTIFICACIONES_BORRADAS);
        return borradas;
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        Propietario p = findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");
        return dashboardVersion.getOrDefault(p.getCedula(), 0L);
    }

    /**
     * Cambia el estado del propietario validando que el nuevo estado sea diferente
     * del actual.
     * Registra siempre una notificación de cambio de estado.
     */
    public void cambiarEstadoDePropietario(Propietario propietario, Estado nuevoEstado) throws OblException {
        if (propietario == null || nuevoEstado == null) {
            throw new OblException("Propietario y estado no pueden ser nulos");
        }

        Estado estadoActual = propietario.getEstadoActual();

        // Validar que el estado nuevo es diferente del actual
        if (estadoActual != null && estadoActual.equals(nuevoEstado)) {
            throw new OblException("El propietario ya esta en estado " + estadoActual.nombre());
        }

        // Cambiar el estado usando el método experto del propietario
        propietario.cambiarEstado(nuevoEstado);

        // Registrar notificación de cambio de estado (siempre, sin importar el estado)
        LocalDateTime ahora = LocalDateTime.now();
        propietario.registrarNotificacionCambioEstado(nuevoEstado, ahora);

        // También registrar en notificaciones globales
        String mensaje = "Se ha cambiado tu estado en el sistema. Tu estado actual es " + nuevoEstado.nombre();
        notificacionesGlobales.add(new Notificacion(ahora, mensaje, propietario));

        // Incrementar versión del dashboard
        dashboardVersion.merge(propietario.getCedula(), 1L, Long::sum);
        // Notifica a las vistas que cambió el estado del propietario
        avisar(Eventos.CAMBIO_ESTADO);
    }
}
