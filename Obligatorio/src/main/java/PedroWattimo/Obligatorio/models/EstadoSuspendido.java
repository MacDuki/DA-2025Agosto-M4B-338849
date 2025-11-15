package PedroWattimo.Obligatorio.models;

/**
 * Estado Suspendido: puede ingresar al sistema, pero no puede realizar
 * tránsitos.
 */
public class EstadoSuspendido extends Estado {

    public EstadoSuspendido() {
        super("Suspendido");
    }

    @Override
    public boolean permiteTransitar() {
        return false;
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
