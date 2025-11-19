package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;
import observador.Observable;

public class Fachada extends Observable {

    private static final Fachada instancia = new Fachada();

    // Sistemas como atributos privados. Nunca gets ni sets de estos atributos.
    // Nueva topología refactorizada
    private SistemaPropietariosYAdmin sistemaPropietariosYAdmin;
    private SistemaVehiculosYCategorias sistemaVehiculosYCategorias;
    private SistemaPuestosYTarifas sistemaPuestosYTarifas;
    private SistemaTransitos sistemaTransitos;
    private SistemaBonificaciones sistemaBonificaciones;
    private SistemaEstados sistemaEstados;
    private SistemaAcceso sistemaAcceso;

    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaPropietariosYAdmin = new SistemaPropietariosYAdmin();
        this.sistemaVehiculosYCategorias = new SistemaVehiculosYCategorias();
        this.sistemaPuestosYTarifas = new SistemaPuestosYTarifas();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaEstados = new SistemaEstados();
        this.sistemaAcceso = new SistemaAcceso();
    }

    public static Fachada getInstancia() {
        return instancia;
    }

    // --------------------------------------------------------------
    // Gestión de Observadores: suscribir observadores a la Fachada
    // --------------------------------------------------------------

    /**
     * Registra un observador en la Fachada que notifica cambios del sistema.
     * Esto permite que los controladores/vistas reaccionen a cambios en el modelo.
     */
    public void registrarObservador(observador.Observador observador) {
        if (observador != null) {
            this.agregarObservador(observador);
        }
    }

    /**
     * Remueve un observador de la Fachada.
     * Útil al cerrar sesión o destruir componentes de UI.
     */
    public void removerObservador(observador.Observador observador) {
        if (observador != null) {
            this.eliminarObservador(observador);
        }
    }

    // --------------------------------------------------------------
    // CU: Agregar Categoría
    // --------------------------------------------------------------

    /**
     * Agrega una nueva categoría al sistema.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public Categoria agregarCategoria(String nombre) throws OblException {
        return sistemaVehiculosYCategorias.agregarCategoria(nombre);
    }

    // --------------------------------------------------------------
    // CU: Agregar Puesto
    // --------------------------------------------------------------

    /**
     * Agrega un nuevo puesto de peaje al sistema.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public Puesto agregarPuesto(String nombre, String direccion) throws OblException {
        return sistemaPuestosYTarifas.agregarPuesto(nombre, direccion);
    }

    // --------------------------------------------------------------
    // CU: Agregar Tarifa a Puesto
    // --------------------------------------------------------------

    /**
     * Agrega una tarifa a un puesto específico.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public void agregarTarifaAPuesto(String nombrePuesto, double monto, String nombreCategoria) throws OblException {
        sistemaPuestosYTarifas.agregarTarifaAPuesto(nombrePuesto, monto, nombreCategoria);
    }

    // --------------------------------------------------------------
    // CU: Agregar Administrador
    // --------------------------------------------------------------

    /**
     * Agrega un nuevo administrador al sistema.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public Administrador agregarAdministrador(int cedula, String nombreCompleto, String password) throws OblException {
        return sistemaPropietariosYAdmin.agregarAdministrador(cedula, nombreCompleto, password);
    }

    // --------------------------------------------------------------
    // CU: Registrar Propietario
    // --------------------------------------------------------------

    /**
     * Registra un nuevo propietario en el sistema.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public Propietario registrarPropietario(int cedula, String nombreCompleto, String password) throws OblException {
        return sistemaPropietariosYAdmin.registrarPropietario(cedula, nombreCompleto, password);
    }

    // --------------------------------------------------------------
    // CU: Registrar Vehículo
    // --------------------------------------------------------------

    /**
     * Registra un vehículo para un propietario.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo, String color,
            String nombreCategoria) throws OblException {
        return sistemaVehiculosYCategorias.registrarVehiculo(cedulaPropietario, matricula, modelo, color,
                nombreCategoria);
    }

    // --------------------------------------------------------------
    // CU: Agregar Bonificación
    // --------------------------------------------------------------

    /**
     * Agrega una nueva bonificación al sistema.
     * Patrón Experto: delega en el subsistema correspondiente.
     */
    public void agregarBonificacion(Bonificacion bonificacion) throws OblException {
        sistemaBonificaciones.agregarBonificacion(bonificacion);
    }

    // --------------------------------------------------------------
    // Métodos auxiliares para consultas
    // --------------------------------------------------------------

    public List<Categoria> obtenerCategorias() {
        return sistemaVehiculosYCategorias.getCategorias();
    }

    public Propietario buscarPropietarioPorCedula(int cedula) {
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

    // --------------------------------------------------------------
    // Métodos internos para comunicación entre subsistemas
    // Solo incluyen métodos que NO tienen equivalente público
    // --------------------------------------------------------------

    /**
     * Método interno: Busca propietario por matrícula de vehículo.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Propietario buscarPropietarioPorMatriculaInterno(String matricula) throws OblException {
        return sistemaPropietariosYAdmin.propietarioPorMatricula(matricula);
    }

    /**
     * Método interno: Obtiene bonificación vigente para un propietario en un
     * puesto.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    java.util.Optional<Bonificacion> obtenerBonificacionVigenteInterno(Propietario propietario, Puesto puesto) {
        return sistemaBonificaciones.bonificacionVigente(propietario, puesto);
    }

    /**
     * Método interno: Registra una notificación para un propietario.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    void registrarNotificacionInterno(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        sistemaPropietariosYAdmin.registrarNotificacion(propietario, mensaje, fechaHora);
    }

    /**
     * Método interno: Busca puesto por nombre.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Puesto buscarPuestoPorNombreInterno(String nombrePuesto) throws OblException {
        return sistemaPuestosYTarifas.obtenerPorNombre(nombrePuesto);
    }

    /**
     * Método interno: Busca estado por nombre.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Estado buscarEstadoPorNombreInterno(String nombreEstado) throws OblException {
        return sistemaEstados.buscarPorNombre(nombreEstado);
    }

    /**
     * Método interno: Autentica y valida propietario.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Propietario autenticarYValidarPropietarioInterno(int cedula, String password) throws OblException {
        return sistemaPropietariosYAdmin.autenticarYValidarPropietario(cedula, password);
    }

    /**
     * Método interno: Autentica y valida administrador.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Administrador autenticarYValidarAdminInterno(int cedula, String password) throws OblException {
        return sistemaPropietariosYAdmin.autenticarYValidarAdmin(cedula, password);
    }

    /**
     * Método interno: Desloguea un administrador.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    void desloguearAdminInterno(Administrador admin) {
        sistemaPropietariosYAdmin.desloguearAdmin(admin);
    }

    /**
     * Método interno: Busca categoría por nombre.
     * Usado por subsistemas para resolver dependencias a través de la Fachada.
     */
    Categoria buscarCategoriaPorNombreInterno(String nombreCategoria) {
        return sistemaVehiculosYCategorias.buscarCategoriaPorNombre(nombreCategoria);
    }

}