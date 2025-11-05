package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaCategorias {
    private List<Categoria> categorias = new ArrayList<>();

    protected SistemaCategorias() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Categoria> getCategorias() {
        return List.copyOf(categorias);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Categoria> obtenerCategoriasInternas() {
        return categorias;
    }
}
