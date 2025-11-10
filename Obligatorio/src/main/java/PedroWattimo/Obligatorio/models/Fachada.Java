package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

public class Fachada {

    private static Fachada instancia;

    // Sistemas como atributos privados. Nunca gets ni sets de estos atributos.
    // Nueva topología refactorizada
    private SistemaPropietarios sistemaPropietarios; // absorbe estados + notificaciones
    private SistemaVehiculosYCategorias sistemaVehiculosYCategorias; // vehiculos + categorias
    private SistemaPuestosYTarifas sistemaPuestosYTarifas; // puestos + tarifas
    private SistemaTransitos sistemaTransitos;
    private SistemaBonificaciones sistemaBonificaciones;
    private SistemaAuth sistemaAuth;

    // Sin estado adicional: toda versión/estado vive en los subsistemas

    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaPropietarios = new SistemaPropietarios();
        this.sistemaVehiculosYCategorias = new SistemaVehiculosYCategorias();
        this.sistemaPuestosYTarifas = new SistemaPuestosYTarifas();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaAuth = new SistemaAuth();

        // Inyecciones según nueva arquitectura
        this.sistemaTransitos.setSistemaPropietarios(this.sistemaPropietarios);
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
        return sistemaAuth.obtenerAdministradoresInternos();
    }

    List<Categoria> obtenerCategoriasInternos() {
        return sistemaVehiculosYCategorias.obtenerCategoriasInternas();
    }

    List<Puesto> obtenerPuestosInternos() {
        return sistemaPuestosYTarifas.obtenerPuestosInternos();
    }

    List<Propietario> obtenerPropietariosInternos() {
        return sistemaPropietarios.obtenerPropietariosInternos();
    }

    List<Vehiculo> obtenerVehiculosInternos() {
        return sistemaVehiculosYCategorias.obtenerVehiculosInternos();
    }

    List<Categoria> obtenerCategorias() {
        return sistemaVehiculosYCategorias.getCategorias();
    }

    Propietario buscarPropietarioPorCedula(String cedula) {
        return sistemaPropietarios.buscarPorCedula(cedula);
    }

    // --------------------------------------------------------------
    // Casos de uso
    // --------------------------------------------------------------

    /**
     * CU: Ingresar a la aplicación (Propietarios)
     * La fachada delega en una línea al SistemaAuth.
     */
    public PropietarioDTO loginPropietario(int cedula, String password) throws OblException {
        return this.sistemaAuth.loginPropietario(this.sistemaPropietarios, cedula, password);
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
        return this.sistemaPropietarios.dashboardDePropietario(cedula);
    }

    public int borrarNotificacionesDePropietario(int cedula) throws OblException {
        return this.sistemaPropietarios.borrarNotificacionesDePropietario(cedula);
    }

    public long versionDashboardDePropietario(int cedula) throws OblException {
        return this.sistemaPropietarios.versionDashboardDePropietario(cedula);
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

}