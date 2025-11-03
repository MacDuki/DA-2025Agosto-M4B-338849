package PedroWattimo.Obligatorio.models;


public class Fachada {

   
    private static Fachada instancia;

    // Sistemas como atributos privados. Nunca gets ni sets de estos atributos.
    private final SistemaEstados sistemaEstados;
    private final SistemaPropietarios sistemaPropietarios;
    private final SistemaVehiculos sistemaVehiculos;
    private final SistemaPuestos sistemaPuestos;
    private final SistemaTransitos sistemaTransitos;
    private final SistemaBonificaciones sistemaBonificaciones;
    private final SistemaAuth sistemaAuth;

  
    private Fachada() {
        // Instanciación de sub-sistemas
        this.sistemaEstados = new SistemaEstados();
        this.sistemaPropietarios = new SistemaPropietarios();
        this.sistemaVehiculos = new SistemaVehiculos();
        this.sistemaPuestos = new SistemaPuestos();
        this.sistemaTransitos = new SistemaTransitos();
        this.sistemaBonificaciones = new SistemaBonificaciones();
        this.sistemaAuth = new SistemaAuth();
    }

    // Acceso controlado a la instancia única
    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    // --------------------------------------------------------------
    // Delegación de métodos a los sistemas internos
    // --------------------------------------------------------------

}