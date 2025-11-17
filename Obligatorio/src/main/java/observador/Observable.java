package observador;

import java.util.ArrayList;

public class Observable {
    private ArrayList<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void eliminarObservador(Observador o) {
        observadores.remove(o);
    }

    public void avisar(Object evento) {
        ArrayList<Observador> copiaObservadores = new ArrayList<>(observadores);
        for (Observador o : copiaObservadores) {
            o.actualizar(this, evento);
        }
    }
}
