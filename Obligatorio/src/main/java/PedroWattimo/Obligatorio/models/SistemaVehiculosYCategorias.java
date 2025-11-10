package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

/**
 * SistemaVehiculosYCategorias: fusiona el antiguo SistemaVehiculos y la
 * colección global de Categorias (antes alojada en SistemaPuestos).
 * Responsable ÚNICAMENTE de proveer acceso a las colecciones y búsquedas
 * simples. No realiza cálculos (mantienen las entidades).
 */
public class SistemaVehiculosYCategorias {
    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();

    protected SistemaVehiculosYCategorias() {
    }

    // ---- Acceso encapsulado ----
    public List<Vehiculo> getVehiculos() {
        return List.copyOf(vehiculos);
    }

    public List<Categoria> getCategorias() {
        return List.copyOf(categorias);
    }

    List<Vehiculo> obtenerVehiculosInternos() {
        return vehiculos;
    }

    List<Categoria> obtenerCategoriasInternas() {
        return categorias;
    }

    // ---- Búsquedas simples ----
    public Categoria buscarCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            return null;
        return categorias.stream()
                .filter(c -> nombre.equalsIgnoreCase(c.getNombre()))
                .findFirst()
                .orElse(null);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        if (matricula == null || matricula.isBlank())
            return null;
        return vehiculos.stream()
                .filter(v -> matricula.equalsIgnoreCase(v.getMatricula()))
                .findFirst()
                .orElse(null);
    }
}
