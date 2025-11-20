package PedroWattimo.Obligatorio.models.subsistemas;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Categoria;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Vehiculo;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaVehiculosYCategorias {
    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();

    protected SistemaVehiculosYCategorias() {
    }

    public List<Vehiculo> getVehiculos() {
        return List.copyOf(vehiculos);
    }

    public List<Categoria> getCategorias() {
        return List.copyOf(categorias);
    }

    public Categoria buscarCategoriaPorNombre(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre de la categoría no puede estar vacío");
        }

        return categorias.stream()
                .filter(c -> nombre.equalsIgnoreCase(c.getNombre()))
                .findFirst()
                .orElseThrow(() -> new OblException("No existe la categoría: " + nombre));
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) throws OblException {
        if (matricula == null || matricula.isBlank()) {
            throw new OblException("La matrícula no puede estar vacía");
        }

        return vehiculos.stream()
                .filter(v -> matricula.equalsIgnoreCase(v.getMatricula()))
                .findFirst()
                .orElseThrow(() -> new OblException("No existe un vehículo con la matrícula: " + matricula));
    }

    public Categoria agregarCategoria(String nombre) throws OblException {
        Categoria.validarDatosCreacion(nombre);

        try {
            buscarCategoriaPorNombre(nombre);

            throw new OblException("Ya existe una categoría con el nombre: " + nombre);
        } catch (OblException e) {

            if (!e.getMessage().startsWith("No existe la categoría")) {
                throw e;
            }
        }

        Categoria nuevaCategoria = new Categoria(nombre);
        categorias.add(nuevaCategoria);
        return nuevaCategoria;
    }

    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo,
            String color, String nombreCategoria) throws OblException {
        Vehiculo.validarDatosCreacion(matricula, modelo, nombreCategoria);

        try {
            buscarVehiculoPorMatricula(matricula);

            throw new OblException("Ya existe un vehículo con la matrícula: " + matricula);
        } catch (OblException e) {

            if (!e.getMessage().startsWith("No existe un vehículo")) {
                throw e;
            }
        }

        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedulaPropietario);
        if (propietario == null) {
            throw new OblException("No existe el propietario con cédula: " + cedulaPropietario);
        }

        Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);

        Vehiculo nuevoVehiculo = propietario.registrarVehiculo(matricula, modelo, color, categoria);
        vehiculos.add(nuevoVehiculo);
        return nuevoVehiculo;
    }
}
