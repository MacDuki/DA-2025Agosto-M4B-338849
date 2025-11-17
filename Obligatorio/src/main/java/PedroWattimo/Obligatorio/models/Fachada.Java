package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;
import observador.Observable;

public class Fachada extends Observable {

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
    private SistemaAcceso sistemaAcceso; // gestión de sesiones y login/logout

    // Sin estado adicional: toda versión/estado vive en los subsistemas

    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaPropietariosYAdmin = new SistemaPropietariosYAdmin();
        this.sistemaVehiculosYCategorias = new SistemaVehiculosYCategorias();
        this.sistemaPuestosYTarifas = new SistemaPuestosYTarifas();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaEstados = new SistemaEstados();
        this.sistemaAcceso = new SistemaAcceso();

        // Inyecciones según nueva arquitectura
        this.sistemaTransitos.setSistemaPropietariosYAdmin(this.sistemaPropietariosYAdmin);
        this.sistemaTransitos.setSistemaPuestosYTarifas(this.sistemaPuestosYTarifas);
        this.sistemaTransitos.setSistemaBonificaciones(this.sistemaBonificaciones);
        this.sistemaAcceso.setSistemaPropietariosYAdmin(this.sistemaPropietariosYAdmin);
        this.sistemaBonificaciones.setSistemaPropietarios(this.sistemaPropietariosYAdmin);
        this.sistemaBonificaciones.setSistemaPuestos(this.sistemaPuestosYTarifas);
        this.sistemaPropietariosYAdmin.setSistemaEstados(this.sistemaEstados);
    }

    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    // --------------------------------------------------------------
    // Gestión de Observadores: suscribir observadores a los sistemas observables
    // --------------------------------------------------------------

    /**
     * Registra un observador en los sistemas de dominio que notifican cambios.
     * Esto permite que los controladores/vistas reaccionen a cambios en el modelo.
     */
    public void registrarObservador(observador.Observador observador) {
        if (observador != null) {
            this.sistemaPropietariosYAdmin.agregarObservador(observador);
            this.sistemaTransitos.agregarObservador(observador);
            this.sistemaBonificaciones.agregarObservador(observador);
        }
    }

    /**
     * Remueve un observador de los sistemas de dominio.
     * Útil al cerrar sesión o destruir componentes de UI.
     */
    public void removerObservador(observador.Observador observador) {
        if (observador != null) {
            this.sistemaPropietariosYAdmin.eliminarObservador(observador);
            this.sistemaTransitos.eliminarObservador(observador);
            this.sistemaBonificaciones.eliminarObservador(observador);
        }
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
     * Retorna la sesión del dominio.
     */
    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        return this.sistemaAcceso.loginPropietario(cedula, password);
    }

    /**
     * CU: Login de Administrador
     * Retorna la sesión del dominio.
     */
    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
        return this.sistemaAcceso.loginAdmin(cedula, password);
    }

    /**
     * CU: Logout de Administrador
     * Delegado en SistemaAcceso.
     */
    public void logoutAdmin(SesionAdmin sesion) {
        this.sistemaAcceso.logoutAdmin(sesion);
    }

    /**
     * CU: Logout de Propietario
     * Delegado en SistemaAcceso.
     */
    public void logoutPropietario(SesionPropietario sesion) {
        this.sistemaAcceso.logoutPropietario(sesion);
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
     * Fachada solo orquesta: delega completamente al sistema de bonificaciones.
     */
    public void asignarBonificacion(String cedula, String nombreBonificacion, String nombrePuesto)
            throws OblException {
        sistemaBonificaciones.asignarBonificacion(cedula, nombreBonificacion, nombrePuesto);
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
     * Fachada solo orquesta: delega completamente al sistema de propietarios.
     */
    public void cambiarEstadoPropietario(String cedulaPropietario, String nombreNuevoEstado) throws OblException {
        sistemaPropietariosYAdmin.cambiarEstadoPropietario(cedulaPropietario, nombreNuevoEstado);
    }

    // --------------------------------------------------------------
    // Métodos para transformación de datos (Patrón Experto)
    // --------------------------------------------------------------

    /**
     * Obtiene un propietario con sus bonificaciones asignadas.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto obtenerPropietarioConBonificaciones(
            String cedula) throws OblException {
        return sistemaPropietariosYAdmin.obtenerPropietarioConBonificaciones(cedula);
    }

    /**
     * Obtiene las tarifas de un puesto como DTOs.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public java.util.List<PedroWattimo.Obligatorio.dtos.TarifaDto> obtenerTarifasDePuesto(Long puestoId)
            throws OblException {
        return sistemaPuestosYTarifas.obtenerTarifasDePuesto(puestoId);
    }

    /**
     * Lista todos los puestos como DTOs.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public java.util.List<PedroWattimo.Obligatorio.dtos.PuestoDto> listarPuestosDto() {
        return sistemaPuestosYTarifas.listarPuestosDto();
    }

    /**
     * Lista todos los estados como DTOs.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public java.util.List<PedroWattimo.Obligatorio.dtos.EstadoDto> listarEstadosDto() {
        return sistemaEstados.listarEstadosDto();
    }

    /**
     * Busca un propietario y devuelve un resumen como DTO.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public PedroWattimo.Obligatorio.dtos.PropietarioResumenDto buscarPropietarioResumenDto(String cedula)
            throws OblException {
        return sistemaPropietariosYAdmin.buscarPropietarioResumenDto(cedula);
    }

    /**
     * Lista todas las bonificaciones como DTOs.
     * Fachada solo orquesta: delega al sistema correspondiente.
     */
    public java.util.List<PedroWattimo.Obligatorio.dtos.BonificacionDto> listarBonificacionesDto() {
        return sistemaBonificaciones.listarBonificacionesDto();
    }

}