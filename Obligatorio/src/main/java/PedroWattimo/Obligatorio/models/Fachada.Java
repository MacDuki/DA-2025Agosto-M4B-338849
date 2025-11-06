package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.NotificacionDto;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.dtos.TransitoDto;
import PedroWattimo.Obligatorio.dtos.VehiculoResumenDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

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

        // Inyectar dependencias en SistemaTransitos
        this.sistemaTransitos.setSistemaPropietarios(this.sistemaPropietarios);
        this.sistemaTransitos.setSistemaPuestos(this.sistemaPuestos);
        this.sistemaTransitos.setSistemaBonificaciones(this.sistemaBonificaciones);
        this.sistemaTransitos.setSistemaNotificaciones(this.sistemaNotificaciones);
    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    List<Administrador> obtenerAdministradoresInternos() {
        return sistemaAuth.obtenerAdministradoresInternos();
    }

    List<Categoria> obtenerCategoriasInternos() {
        return sistemaPuestos.obtenerCategoriasInternas();
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
        return sistemaPuestos.getCategorias();
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
    public PropietarioDTO loginPropietario(int cedula, String password) throws OblException {

        String cedulaStr = String.valueOf(cedula);
        Propietario propietario = this.sistemaAuth.autenticarPropietario(this.sistemaPropietarios, cedulaStr, password);

        if (!propietario.puedeIngresar()) {
            throw new OblException("Usuario deshabilitado, no puede ingresar al sistema");
        }

        return new PropietarioDTO(
                propietario.getCedula(),
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre()
                        : Estado.HABILITADO.nombre());
    }

    /**
     * CU: Login de Administrador
     * Delegado en SistemaAuth. Sin lógica de hashing aquí.
     */
    public AdminAutenticadoDto loginAdmin(int cedula, String password) throws OblException {
        return this.sistemaAuth.loginAdmin(cedula, password);
    }

    /**
     * CU: Logout de Administrador
     */
    public void logoutAdmin(int cedula) throws OblException {
        this.sistemaAuth.logoutAdmin(cedula);
    }

    // --------------------------------------------------------------
    // CU: Dashboard del propietario
    // --------------------------------------------------------------
    public PropietarioDashboardDto dashboardDePropietario(int cedula) throws OblException {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");

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

    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");
        int borradas = p.borrarNotificaciones();
        // Persist in-memory already done; aumentar versión
        dashboardVersion.merge(p.getCedula(), 1L, Long::sum);
        return borradas;
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        Propietario p = this.sistemaPropietarios.findByCedulaWithVehiculosTransitosBonificacionesNotificaciones(cedula);
        if (p == null)
            throw new OblException("El propietario no existe");
        return dashboardVersion.getOrDefault(p.getCedula(), 0L);
    }

    // --------------------------------------------------------------
    // CU: Emular tránsito
    // --------------------------------------------------------------
    /**
     * CU: Emular tránsito.
     * Fachada como único punto de entrada: delega en SistemaTransitos con una sola
     * línea.
     */
    public EmularTransitoResultado emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {
        return sistemaTransitos.emularTransito(puestoId, matricula, fechaHora);
    }

    /**
     * Obtiene la lista de puestos.
     * Delegado en SistemaPuestos.
     */
    public List<Puesto> obtenerPuestos() {
        return sistemaPuestos.getPuestos();
    }

    /**
     * Obtiene un puesto por su ID.
     * Delegado en SistemaPuestos.
     */
    public Puesto obtenerPuestoPorId(Long id) throws OblException {
        return sistemaPuestos.obtenerPorId(id);
    }

}