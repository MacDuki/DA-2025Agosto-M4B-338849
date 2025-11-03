package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaEstados {
    private List<Estado> estados = new ArrayList<Estado>();

    protected SistemaEstados() {

    }

    public List<Estado> getEstados() {
        return estados;
    }
}
