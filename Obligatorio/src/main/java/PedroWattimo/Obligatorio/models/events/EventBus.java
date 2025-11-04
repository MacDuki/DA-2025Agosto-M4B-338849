package PedroWattimo.Obligatorio.models.events;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instancia;

    public static EventBus getInstancia() {
        if (instancia == null)
            instancia = new EventBus();
        return instancia;
    }

    private final Set<Consumer<DashboardActualizadoEvent>> dashboardListeners = ConcurrentHashMap.newKeySet();

    public void publish(DashboardActualizadoEvent event) {
        for (Consumer<DashboardActualizadoEvent> l : dashboardListeners) {
            try {
                l.accept(event);
            } catch (Exception ignored) {
            }
        }
    }

    public AutoCloseable subscribeDashboard(Consumer<DashboardActualizadoEvent> listener) {
        dashboardListeners.add(listener);
        return () -> dashboardListeners.remove(listener);
    }
}
