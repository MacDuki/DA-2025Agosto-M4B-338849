package PedroWattimo.Obligatorio.models.entidades;

import PedroWattimo.Obligatorio.models.fabricas.FabricaEstados;

/***
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

    public abstract boolean permiteTransitar();

    public abstract boolean permiteBonificaciones();

    public abstract boolean permiteNotificaciones();

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
