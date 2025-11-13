package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

public class Fachada {

    private static Fachada instancia;

    // Sistemas como atributos privados. Nunca gets ni sets de estos atributos.
    // Nueva topología refactorizada
    private SistemaPropietariosYAdmin sistemaPropietariosYAdmin; // absorbe auth + propietarios + estados +
                                                                 // notificaciones
    private SistemaVehiculosYCategorias sistemaVehiculosYCategorias; // vehiculos + categorias
    private SistemaPuestosYTarifas sistemaPuestosYTarifas; // puestos + tarifas
    private SistemaTransitos sistemaTransitos;
    private SistemaBonificaciones sistemaBonificaciones;
    private SistemaEstados sistemaEstados; // gestión de estados

    // Sin estado adicional: toda versión/estado vive en los subsistemas

    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaPropietariosYAdmin = new SistemaPropietariosYAdmin();
        this.sistemaVehiculosYCategorias = new SistemaVehiculosYCategorias();
        this.sistemaPuestosYTarifas = new SistemaPuestosYTarifas();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaEstados = new SistemaEstados();

        // Inyecciones según nueva arquitectura
        this.sistemaTransitos.setSistemaPropietariosYAdmin(this.sistemaPropietariosYAdmin);
        this.sistemaTransitos.setSistemaPuestosYTarifas(this.sistemaPuestosYTarifas);
        this.sistemaTransitos.setSistemaBonificaciones(this.sistemaBonificaciones);
    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    List<Administrador> obtenerAdministradoresInternos() {
        return sistemaPropietariosYAdmin.obtenerAdministradoresInternos();
    }

    List<Categoria> obtenerCategoriasInternos() {
        return sistemaVehiculosYCategorias.obtenerCategoriasInternas();
    }

    List<Puesto> obtenerPuestosInternos() {
        return sistemaPuestosYTarifas.obtenerPuestosInternos();
    }

    List<Propietario> obtenerPropietariosInternos() {
        return sistemaPropietariosYAdmin.obtenerPropietariosInternos();
    }

    List<Vehiculo> obtenerVehiculosInternos() {
        return sistemaVehiculosYCategorias.obtenerVehiculosInternos();
    }

    List<Bonificacion> obtenerBonificacionesInternas() {
        return sistemaBonificaciones.obtenerBonificacionesInternas();
    }

    List<AsignacionBonificacion> obtenerAsignacionesBonificacionesInternas() {
        return sistemaBonificaciones.obtenerAsignacionesInternas();
    }

    List<Categoria> obtenerCategorias() {
        return sistemaVehiculosYCategorias.getCategorias();
    }

    Propietario buscarPropietarioPorCedula(int cedula) {
        return sistemaPropietariosYAdmin.buscarPorCedula(cedula);
    }

    // --------------------------------------------------------------
    // Casos de uso
    // --------------------------------------------------------------

    /**
     * CU: Ingresar a la aplicación (Propietarios)
     * La fachada delega en una línea al SistemaPropietariosYAdmin.
     */
    public PropietarioAutenticadoDTO loginPropietario(int cedula, String password) throws OblException {
        SesionPropietario sesion = this.sistemaPropietariosYAdmin.loginPropietario(cedula, password);
        Propietario p = sesion.getPropietario();
        return new PropietarioAutenticadoDTO(
                p.getCedula(),
                p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : Estado.HABILITADO.nombre());
    }

    /**
     * CU: Login de Administrador
     * Delegado en SistemaPropietariosYAdmin.
     */
    public AdminAutenticadoDto loginAdmin(int cedula, String password) throws OblException {
        SesionAdmin sesion = this.sistemaPropietariosYAdmin.loginAdmin(cedula, password);
        Administrador admin = sesion.getAdministrador();
        return new AdminAutenticadoDto(admin.getCedula(), admin.getNombreCompleto());
    }

    /**
     * CU: Logout de Administrador
     */
    public void logoutAdmin(int cedula) throws OblException {
        this.sistemaPropietariosYAdmin.logoutAdmin(cedula);
    }

    // --------------------------------------------------------------
    // CU: Dashboard del propietario
    // --------------------------------------------------------------
    public PropietarioDashboardDto dashboardDePropietario(int cedula) throws OblException {
        return this.sistemaPropietariosYAdmin.dashboardDePropietario(cedula);
    }

    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        return this.sistemaPropietariosYAdmin.borrarNotificacionesDePropietario(cedula);
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        return this.sistemaPropietariosYAdmin.versionDashboardDePropietario(cedula);
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
        return sistemaPuestosYTarifas.getPuestos();
    }

    /**
     * Obtiene un puesto por su ID.
     * Delegado en SistemaPuestos.
     */
    public Puesto obtenerPuestoPorId(Long id) throws OblException {
        return sistemaPuestosYTarifas.obtenerPorId(id);
    }

    // --------------------------------------------------------------
    // CU: Asignar bonificación a propietario
    // --------------------------------------------------------------

    /**
     * Lista todas las bonificaciones definidas en el sistema.
     */
    public List<Bonificacion> listarBonificaciones() {
        return sistemaBonificaciones.listarBonificaciones();
    }

    /**
     * Lista todos los puestos del sistema.
     */
    public List<Puesto> listarPuestos() {
        return sistemaPuestosYTarifas.listarPuestos();
    }

    /**
     * Busca un propietario por cédula.
     * Lanza OblException si no existe.
     */
    public Propietario buscarPropietarioPorCedula(String cedula) throws OblException {
        return sistemaPropietariosYAdmin.buscarPorCedula(cedula);
    }

    /**
     * Asigna una bonificación a un propietario para un puesto específico.
     */
    public void asignarBonificacion(String cedula, String nombreBonificacion, String nombrePuesto)
            throws OblException {
        Propietario propietario = sistemaPropietariosYAdmin.buscarPorCedula(cedula);
        Bonificacion bonificacion = sistemaBonificaciones.buscarPorNombre(nombreBonificacion);
        Puesto puesto = sistemaPuestosYTarifas.obtenerPorNombre(nombrePuesto);
        sistemaBonificaciones.asignarBonificacionAPropietario(propietario, bonificacion, puesto);
    }

    // --------------------------------------------------------------
    // CU: Cambiar estado de propietario
    // --------------------------------------------------------------

    /**
     * Lista todos los estados disponibles en el sistema.
     */
    public List<Estado> listarEstados() {
        return sistemaEstados.listarEstados();
    }

    /**
     * Cambia el estado de un propietario.
     * Valida que el propietario exista y que el nuevo estado sea diferente del
     * actual.
     */
    public void cambiarEstadoPropietario(String cedulaPropietario, String nombreNuevoEstado) throws OblException {
        Propietario propietario = sistemaPropietariosYAdmin.buscarPorCedula(cedulaPropietario);
        Estado nuevoEstado = sistemaEstados.buscarPorNombre(nombreNuevoEstado);
        sistemaPropietariosYAdmin.cambiarEstadoDePropietario(propietario, nuevoEstado);
    }

}