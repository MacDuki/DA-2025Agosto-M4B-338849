package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.NotificacionDto;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.dtos.TransitoDto;
import PedroWattimo.Obligatorio.dtos.VehiculoResumenDto;
import PedroWattimo.Obligatorio.models.exceptions.PropietarioNoExisteException;
import PedroWattimo.Obligatorio.models.exceptions.UsuarioDeshabilitadoException;

public class Fachada {

    private static Fachada instancia;

    // Sistemas como atributos privados. Nunca gets ni sets de estos atributos.
    private SistemaEstados sistemaEstados;
    private SistemaPropietarios sistemaPropietarios;
    private SistemaVehiculos sistemaVehiculos;
    private SistemaPuestos sistemaPuestos;
    private SistemaTransitos sistemaTransitos;
    private SistemaBonificaciones sistemaBonificaciones;
    private SistemaAuth sistemaAuth;
    private SistemaNotificaciones sistemaNotificaciones;
    private SistemaAdministradores sistemaAdministradores;
    private SistemaCategorias sistemaCategorias;

    private final Map<String, Long> dashboardVersion = new ConcurrentHashMap<>();

    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaEstados = new SistemaEstados();
        this.sistemaPropietarios = new SistemaPropietarios();
        this.sistemaVehiculos = new SistemaVehiculos();
        this.sistemaPuestos = new SistemaPuestos();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaAuth = new SistemaAuth();
        this.sistemaNotificaciones = new SistemaNotificaciones();
        this.sistemaAdministradores = new SistemaAdministradores();
        this.sistemaCategorias = new SistemaCategorias();
    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    List<Administrador> obtenerAdministradoresInternos() {
        return sistemaAdministradores.obtenerAdministradoresInternos();
    }

    List<Categoria> obtenerCategoriasInternos() {
        return sistemaCategorias.obtenerCategoriasInternas();
    }

    List<Puesto> obtenerPuestosInternos() {
        return sistemaPuestos.obtenerPuestosInternos();
    }

    List<Propietario> obtenerPropietariosInternos() {
        return sistemaPropietarios.obtenerPropietariosInternos();
    }

    List<Vehiculo> obtenerVehiculosInternos() {
        return sistemaVehiculos.obtenerVehiculosInternos();
    }

    List<Categoria> obtenerCategorias() {
        return sistemaCategorias.getCategorias();
    }

    List<Puesto> obtenerPuestos() {
        return sistemaPuestos.getPuestos();
    }

    Propietario buscarPropietarioPorCedula(String cedula) {
        return sistemaPropietarios.buscarPorCedula(cedula);
    }

    // --------------------------------------------------------------
    // Casos de uso
    // --------------------------------------------------------------

    /**
     * CU: Ingresar a la aplicación (Propietarios)
     * Orquesta la autenticación a través de SistemaAuth y verifica estado del
     * usuario.
     */
    public PropietarioDTO loginPropietario(int cedula, String password) {

        String cedulaStr = String.valueOf(cedula);
        Propietario propietario = this.sistemaAuth.autenticarPropietario(this.sistemaPropietarios, cedulaStr, password);

        if (!propietario.puedeIngresar()) {
            throw new UsuarioDeshabilitadoException();
        }

        return new PropietarioDTO(
                propietario.getCedula(),
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                        : Estado.HABILITADO.nombre());
    }

    // --------------------------------------------------------------
    // CU: Dashboard del propietario
    // --------------------------------------------------------------
    public PropietarioDashboardDto dashboardDePropietario(int cedula) {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new PropietarioNoExisteException();

        PropietarioDashboardDto dto = new PropietarioDashboardDto();
        dto.setPropietario(new PropietarioResumenDto(p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : Estado.HABILITADO.nombre(),
                p.getSaldoActual()));

        // Bonificaciones asignadas (histórico visible aún si penalizado)
        List<BonificacionAsignadaDto> bonos = new ArrayList<>();
        for (AsignacionBonificacion ab : p.bonificacionesAsignadas()) {
            String nombre = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
            String puesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
            bonos.add(new BonificacionAsignadaDto(nombre, puesto, ab.getFechaHora()));
        }
        dto.setBonificaciones(bonos);

        // Vehículos con agregados
        List<VehiculoResumenDto> vehs = new ArrayList<>();
        for (Vehiculo v : p.vehiculos()) {
            vehs.add(new VehiculoResumenDto(
                    v.getMatricula(), v.getModelo(), v.getColor(),
                    p.cantidadTransitosDe(v), p.totalGastadoPor(v)));
        }
        dto.setVehiculos(vehs);

        // Tránsitos ordenados desc por fecha
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

        // Notificaciones ordenadas desc
        List<Notificacion> notifs = new ArrayList<>(p.notificacionesOrdenadasDesc());
        notifs.sort(Comparator.comparing(Notificacion::getFechaHora).reversed());
        List<NotificacionDto> notifDtos = new ArrayList<>();
        for (Notificacion n : notifs) {
            notifDtos.add(new NotificacionDto(n.getFechaHora(), n.getMensaje()));
        }
        dto.setNotificaciones(notifDtos);

        // Versión
        long ver = dashboardVersion.getOrDefault(p.getCedula(), 0L);
        dto.setVersion(ver);
        return dto;
    }

    public int borrarNotificacionesDePropietario(int cedula) {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new PropietarioNoExisteException();
        int borradas = p.borrarNotificaciones();
        // Persist in-memory already done; aumentar versión
        dashboardVersion.merge(p.getCedula(), 1L, Long::sum);
        return borradas;
    }

    public long versionDashboardDePropietario(int cedula) {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new PropietarioNoExisteException();
        return dashboardVersion.getOrDefault(p.getCedula(), 0L);
    }

}