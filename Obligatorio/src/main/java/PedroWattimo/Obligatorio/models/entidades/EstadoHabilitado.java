package PedroWattimo.Obligatorio.models.entidades;

/**
 * Estado Habilitado: estado por defecto, puede usar todas las funcionalidades.
 */
public class EstadoHabilitado extends Estado {

    public EstadoHabilitado() {
        super("Habilitado");
    }

    @Override
    public boolean permiteTransitar() {
        return true;
    }

    @Override
    public boolean permiteBonificaciones() {
        return true;
    }

    @Override
    public boolean permiteNotificaciones() {
        return true;
    }

    @Override
    public boolean permiteIngresar() {
        return true;
    }
}
