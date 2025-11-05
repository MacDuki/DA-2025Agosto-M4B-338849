package PedroWattimo.Obligatorio.models.events;

/**
 * EventBus minimal en memoria.
 *
 * Se mantiene la clase como punto de extensión futuro, pero se elimina la
 * lógica específica del evento "DashboardActualizado".
 */
public class EventBus {
    private static EventBus instancia;

    private EventBus() {
    }

    public static synchronized EventBus getInstancia() {
        if (instancia == null)
            instancia = new EventBus();
        return instancia;
    }
}
