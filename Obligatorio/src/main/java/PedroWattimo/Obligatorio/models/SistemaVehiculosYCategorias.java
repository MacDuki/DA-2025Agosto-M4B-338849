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
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la categoría no puede estar vacío");
        }

        // Validar que no exista
        Categoria existente = buscarCategoriaPorNombre(nombre);
        if (existente != null) {
            throw new OblException("Ya existe una categoría con el nombre: " + nombre);
        }

        Categoria nuevaCategoria = new Categoria(nombre);
        categorias.add(nuevaCategoria);
        return nuevaCategoria;
    }

    // Inyección de dependencia para resolver propietarios
    private SistemaPropietariosYAdmin sistemaPropietarios;

    public void setSistemaPropietarios(SistemaPropietariosYAdmin sistema) {
        this.sistemaPropietarios = sistema;
    }

    /**
     * Registra un vehículo para un propietario.
     * Orquesta la resolución de objetos y delega al propietario (experto).
     */
    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo,
            String color, String nombreCategoria) throws OblException {
        // Validaciones
        if (matricula == null || matricula.isBlank()) {
            throw new OblException("La matrícula no puede estar vacía");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new OblException("El modelo no puede estar vacío");
        }
        if (nombreCategoria == null || nombreCategoria.isBlank()) {
            throw new OblException("La categoría no puede estar vacía");
        }

        // Validar que no exista el vehículo
        Vehiculo existente = buscarVehiculoPorMatricula(matricula);
        if (existente != null) {
            throw new OblException("Ya existe un vehículo con la matrícula: " + matricula);
        }

        // Resolver propietario
        Propietario propietario = sistemaPropietarios.buscarPorCedula(cedulaPropietario);
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
