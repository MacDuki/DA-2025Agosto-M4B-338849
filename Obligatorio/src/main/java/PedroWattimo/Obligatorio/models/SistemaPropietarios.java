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
 * SistemaPropietarios (refactor): absorbe responsabilidades de
 * - SistemaEstados (colección de estados globales ya no necesarias)
 * - SistemaNotificaciones (registro centralizado redundante: las notificaciones
 * viven en el Propietario)
 *
 * Mantiene SOLO orquestación mínima y acceso a la colección raíz de
 * Propietarios.
 * No incluye lógica de negocio: los cálculos permanecen en las entidades
 * (Propietario, Bonificacion, Transito).
 */
public class SistemaPropietarios {
    private final List<Propietario> propietarios = new ArrayList<>();

    // Historial plano de notificaciones globales (opcional para auditoría).
    // Se mantiene como lista separada solo para trazabilidad, sin mover lógica.
    private final List<Notificacion> notificacionesGlobales = new ArrayList<>();
    private final Map<String, Long> dashboardVersion = new ConcurrentHashMap<>();

    protected SistemaPropietarios() {
    }

    // -------- Accesos Encapsulados --------
    public List<Propietario> getPropietarios() {
        return List.copyOf(propietarios);
    }

    List<Propietario> obtenerPropietariosInternos() {
        return propietarios;
    }

    /** Acceso sólo lectura a trazabilidad de notificaciones globales. */
    public List<Notificacion> getNotificacionesGlobales() {
        return List.copyOf(notificacionesGlobales);
    }

    // -------- Operaciones de Orquestación (sin lógica de cálculo) --------
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

    /** Repositorio compuesto en memoria (ya embebido). */
    public Propietario findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(int cedula) {
        return buscarPorCedula(String.valueOf(cedula));
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
     * agrega a trazabilidad global (sin lógica de alerta aquí).
     */
    public void registrarNotificacion(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        if (propietario == null || mensaje == null || fechaHora == null)
            return;
        propietario.registrarNotificacion(mensaje, fechaHora); // Delegación a entidad
        notificacionesGlobales.add(new Notificacion(fechaHora, mensaje, propietario));
    }

    // -------- Casos de uso orientados a Propietario (DTOs) --------
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
