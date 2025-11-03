package PedroWattimo.Obligatorio.models;


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