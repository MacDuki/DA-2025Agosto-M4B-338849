package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaCategorias {
    private List<Categoria> categorias = new ArrayList<>();

    protected SistemaCategorias() {
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }
}
