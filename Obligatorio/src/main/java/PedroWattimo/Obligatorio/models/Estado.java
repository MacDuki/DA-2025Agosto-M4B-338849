package PedroWattimo.Obligatorio.models;

/**
 * Clase abstracta que representa un estado de propietario en el sistema de
 * peajes.
 * Patrón State: cada tipo de estado implementa su propia lógica de permisos.
 */
public abstract class Estado {

    protected String nombre;

    public static final Estado HABILITADO = FabricaEstados.crearHabilitado();
    public static final Estado PENALIZADO = FabricaEstados.crearPenalizado();
    public static final Estado SUSPENDIDO = FabricaEstados.crearSuspendido();
    public static final Estado DESHABILITADO = FabricaEstados.crearDeshabilitado();

    protected Estado(String nombre) {
        this.nombre = nombre;
    }

    public String nombre() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Patrón Experto: el Estado sabe si permite transitar.
     */
    public abstract boolean permiteTransitar();

    /**
     * Patrón Experto: el Estado sabe si permite aplicar bonificaciones.
     */
    public abstract boolean permiteBonificaciones();

    /**
     * Patrón Experto: el Estado sabe si permite registrar notificaciones.
     */
    public abstract boolean permiteNotificaciones();

    /**
     * Patrón Experto: el Estado sabe si permite ingresar al sistema.
     */
    public abstract boolean permiteIngresar();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Estado e))
            return false;
        return nombre.equals(e.nombre);
    }

}
