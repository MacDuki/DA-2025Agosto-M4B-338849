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

/**
 * SistemaPropietariosYAdmin: fusión de SistemaAuth y SistemaPropietarios.
 * Concentra la lógica de autenticación de propietarios y administradores,
 * así como la gestión de propietarios, notificaciones y dashboards.
 */
public class SistemaPropietariosYAdmin {
    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();
    private final List<SesionPropietario> sesionesPropietarios = new ArrayList<>();
    private final List<SesionAdmin> sesionesAdmin = new ArrayList<>();

    // Historial plano de notificaciones globales (opcional para auditoría).
    private final List<Notificacion> notificacionesGlobales = new ArrayList<>();
    private final Map<Integer, Long> dashboardVersion = new ConcurrentHashMap<>();

    protected SistemaPropietariosYAdmin() {
    }

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
    /** Busca un propietario por su cédula. */
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
     */
    public void registrarNotificacion(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        if (propietario == null || mensaje == null || fechaHora == null)
            return;
        propietario.registrarNotificacion(mensaje, fechaHora);
        notificacionesGlobales.add(new Notificacion(fechaHora, mensaje, propietario));
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

    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        Propietario p = autenticarPropietario(cedula, password);
        if (!p.puedeIngresar()) {
            throw new OblException("Usuario deshabilitado, no puede ingresar al sistema");
        }

        SesionPropietario sesion = new SesionPropietario(LocalDateTime.now(), p);
        sesionesPropietarios.add(sesion);
        return sesion;
    }

    public void logoutPropietario(int cedula) {
        // Eliminar la sesión del propietario
        sesionesPropietarios.removeIf(s -> s.getPropietario().getCedula() == cedula);
    }

    // -------- Autenticación de Administradores --------
    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
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
        SesionAdmin sesion = new SesionAdmin(LocalDateTime.now(), admin);
        sesionesAdmin.add(sesion);
        return sesion;
    }

    public void logoutAdmin(int cedula) throws OblException {
        Administrador admin = null;
        for (Administrador a : this.administradores) {
            if (a != null && a.getCedula() == cedula) {
                admin = a;
                break;
            }
        }
        if (admin == null) {
            throw new OblException("Acceso denegado");
        }
        admin.desloguear();

        // Eliminar la sesión del administrador
        sesionesAdmin.removeIf(s -> s.getAdministrador().getCedula() == cedula);
    }

    // -------- Dashboard de Propietario --------
    public PropietarioDashboardDto dashboardDePropietario(int cedula) throws OblException {
        Propietario p = findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");

        PropietarioDashboardDto dto = new PropietarioDashboardDto();
        dto.setPropietario(new PropietarioResumenDto(p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : Estado.HABILITADO.nombre(),
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
        return borradas;
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        Propietario p = findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");
        return dashboardVersion.getOrDefault(p.getCedula(), 0L);
    }
}
