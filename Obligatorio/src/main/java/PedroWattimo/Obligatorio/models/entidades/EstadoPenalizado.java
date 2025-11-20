package PedroWattimo.Obligatorio.models.entidades;

/**
 * Estado Penalizado: puede ingresar al sistema, puede realizar tránsitos,
 * pero no se aplican bonificaciones y no se registran notificaciones.
 */
public class EstadoPenalizado extends Estado {

    public EstadoPenalizado() {
        super("Penalizado");
    }

    @Override
    public boolean permiteTransitar() {
        return true;
    }

    @Override
    public boolean permiteBonificaciones() {
        return false;
    }

    @Override
    public boolean permiteNotificaciones() {
        return false;
    }

    @Override
    public boolean permiteIngresar() {
        return true;
    }
}
