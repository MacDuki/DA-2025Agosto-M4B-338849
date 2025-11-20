package PedroWattimo.Obligatorio.models.subsistemas;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Administrador;
import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.Categoria;
import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Puesto;
import PedroWattimo.Obligatorio.models.entidades.Transito;
import PedroWattimo.Obligatorio.models.entidades.Vehiculo;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;
import PedroWattimo.Obligatorio.models.sesiones.SesionAdmin;
import PedroWattimo.Obligatorio.models.sesiones.SesionPropietario;
import observador.Observable;

public class Fachada extends Observable {

    private static final Fachada instancia = new Fachada();

    // Sistemas como atributos privados.
    // Nunca gets ni sets de estos atributos.
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

    public Categoria agregarCategoria(String nombre) throws OblException {
        return sistemaVehiculosYCategorias.agregarCategoria(nombre);
    }

    public Puesto agregarPuesto(String nombre, String direccion) throws OblException {
        return sistemaPuestosYTarifas.agregarPuesto(nombre, direccion);
    }

    public void agregarTarifaAPuesto(String nombrePuesto, double monto, String nombreCategoria) throws OblException {
        sistemaPuestosYTarifas.agregarTarifaAPuesto(nombrePuesto, monto, nombreCategoria);
    }

    public Administrador agregarAdministrador(int cedula, String nombreCompleto, String password) throws OblException {
        return sistemaPropietariosYAdmin.agregarAdministrador(cedula, nombreCompleto, password);
    }

    public Propietario registrarPropietario(int cedula, String nombreCompleto, String password) throws OblException {
        return sistemaPropietariosYAdmin.registrarPropietario(cedula, nombreCompleto, password);
    }

    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo, String color,
            String nombreCategoria) throws OblException {
        return sistemaVehiculosYCategorias.registrarVehiculo(cedulaPropietario, matricula, modelo, color,
                nombreCategoria);
    }

    public void agregarBonificacion(Bonificacion bonificacion) throws OblException {
        sistemaBonificaciones.agregarBonificacion(bonificacion);
    }

    public List<Categoria> listarCategorias() {
        return sistemaVehiculosYCategorias.getCategorias();
    }

    public Propietario buscarPropietarioPorCedula(int cedula) throws OblException {
        return sistemaPropietariosYAdmin.buscarPorCedula(cedula);
    }

    public SesionPropietario loginPropietario(int cedula, String password) throws OblException {
        return this.sistemaAcceso.loginPropietario(cedula, password);
    }

    public SesionAdmin loginAdmin(int cedula, String password) throws OblException {
        return this.sistemaAcceso.loginAdmin(cedula, password);
    }

    public void logoutAdmin(SesionAdmin sesion) {
        this.sistemaAcceso.logoutAdmin(sesion);
    }

    public void logoutPropietario(SesionPropietario sesion) {
        this.sistemaAcceso.logoutPropietario(sesion);
    }

    // --------------------------------------------------------------
    // CU: Dashboard del propietario
    // --------------------------------------------------------------
    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        return this.sistemaPropietariosYAdmin.borrarNotificacionesDePropietario(cedula);
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        return this.sistemaPropietariosYAdmin.versionDashboardDePropietario(cedula);
    }

    // --------------------------------------------------------------
    // CU: Emular tránsito
    // --------------------------------------------------------------

    public Transito emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {
        return sistemaTransitos.emularTransito(puestoId, matricula, fechaHora);
    }

    public List<Puesto> listarPuestos() {
        return sistemaPuestosYTarifas.listarPuestos();
    }

    public Puesto buscarPuestoPorId(Long id) throws OblException {
        return sistemaPuestosYTarifas.buscarPorId(id);
    }

    // --------------------------------------------------------------
    // CU: Asignar bonificación a propietario
    // --------------------------------------------------------------

    public List<Bonificacion> listarBonificaciones() {
        return sistemaBonificaciones.listarBonificaciones();
    }

    public Propietario buscarPropietarioPorCedula(String cedula) throws OblException {
        return sistemaPropietariosYAdmin.buscarPorCedula(cedula);
    }

    public void asignarBonificacion(String cedula, String nombreBonificacion, String nombrePuesto)
            throws OblException {
        sistemaBonificaciones.asignarBonificacion(cedula, nombreBonificacion, nombrePuesto);
    }

    // --------------------------------------------------------------
    // CU: Cambiar estado de propietario
    // --------------------------------------------------------------

    public List<Estado> listarEstados() {
        return sistemaEstados.listarEstados();
    }

    public void cambiarEstadoPropietario(String cedulaPropietario, String nombreNuevoEstado) throws OblException {
        sistemaPropietariosYAdmin.cambiarEstadoPropietario(cedulaPropietario, nombreNuevoEstado);
    }

    // --------------------------------------------------------------
    // Métodos internos para comunicación entre subsistemas
    // Solo incluyen métodos que NO tienen equivalente público
    // --------------------------------------------------------------

    Propietario buscarPropietarioPorMatriculaInterno(String matricula) throws OblException {
        return sistemaPropietariosYAdmin.propietarioPorMatricula(matricula);
    }

    java.util.Optional<Bonificacion> obtenerBonificacionVigenteInterno(Propietario propietario, Puesto puesto) {
        return sistemaBonificaciones.bonificacionVigente(propietario, puesto);
    }

    void registrarNotificacionInterno(Propietario propietario, String mensaje, LocalDateTime fechaHora) {
        sistemaPropietariosYAdmin.registrarNotificacion(propietario, mensaje, fechaHora);
    }

    Puesto buscarPuestoPorNombreInterno(String nombrePuesto) throws OblException {
        return sistemaPuestosYTarifas.buscarPorNombre(nombrePuesto);
    }

    Estado buscarEstadoPorNombreInterno(String nombreEstado) throws OblException {
        return sistemaEstados.buscarPorNombre(nombreEstado);
    }

    Propietario autenticarYValidarPropietarioInterno(int cedula, String password) throws OblException {
        return sistemaPropietariosYAdmin.autenticarYValidarPropietario(cedula, password);
    }

    Administrador autenticarYValidarAdminInterno(int cedula, String password) throws OblException {
        return sistemaPropietariosYAdmin.autenticarYValidarAdmin(cedula, password);
    }

    void desloguearAdminInterno(Administrador admin) {
        sistemaPropietariosYAdmin.desloguearAdmin(admin);
    }

    Categoria buscarCategoriaPorNombreInterno(String nombreCategoria) {
        return sistemaVehiculosYCategorias.buscarCategoriaPorNombre(nombreCategoria);
    }

}
