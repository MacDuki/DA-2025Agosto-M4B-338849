package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * SistemaVehiculosYCategorias:
 * Responsable ÚNICAMENTE de proveer acceso a las colecciones y búsquedas
 * simples. No realiza cálculos .
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

    // ---- Casos de uso: Alta de entidades ----

    /**
     * Agrega una nueva categoría al sistema.
     * Valida que no exista una categoría con el mismo nombre.
     */
    public Categoria agregarCategoria(String nombre) throws OblException {
        // Validar datos de creación (delegado al experto)
        Categoria.validarDatosCreacion(nombre);

        // Validar unicidad (responsabilidad del sistema)
        Categoria existente = buscarCategoriaPorNombre(nombre);
        if (existente != null) {
            throw new OblException("Ya existe una categoría con el nombre: " + nombre);
        }

        Categoria nuevaCategoria = new Categoria(nombre);
        categorias.add(nuevaCategoria);
        return nuevaCategoria;
    }

    /**
     * Registra un vehículo para un propietario.
     * Orquesta la resolución de objetos y delega al propietario (experto).
     */
    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo,
            String color, String nombreCategoria) throws OblException {
        // Validar datos de creación (delegado al experto)
        Vehiculo.validarDatosCreacion(matricula, modelo, nombreCategoria);

        // Validar unicidad (responsabilidad del sistema)
        Vehiculo existente = buscarVehiculoPorMatricula(matricula);
        if (existente != null) {
            throw new OblException("Ya existe un vehículo con la matrícula: " + matricula);
        }

        // Resolver propietario a través de Fachada
        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedulaPropietario);
        if (propietario == null) {
            throw new OblException("No existe el propietario con cédula: " + cedulaPropietario);
        }

        // Resolver categoría
        Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);
        if (categoria == null) {
            throw new OblException("No existe la categoría: " + nombreCategoria);
        }

        // Delegar al propietario (experto) el registro del vehículo
        Vehiculo nuevoVehiculo = propietario.registrarVehiculo(matricula, modelo, color, categoria);
        vehiculos.add(nuevoVehiculo);
        return nuevoVehiculo;
    }
}
