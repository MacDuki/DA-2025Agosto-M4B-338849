package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * SistemaEstados: gestiona los estados posibles de los propietarios.
 * Proporciona operaciones para listar estados y buscar por nombre.
 */
public class SistemaEstados {
    private final List<Estado> estados;

    protected SistemaEstados() {
        this.estados = new ArrayList<>();
        // Inicializar con los estados disponibles
        this.estados.add(Estado.HABILITADO);
        this.estados.add(Estado.PENALIZADO);
        this.estados.add(Estado.SUSPENDIDO);
        this.estados.add(Estado.DESHABILITADO);
    }

    /**
     * Lista todos los estados disponibles en el sistema.
     */
    public List<Estado> listarEstados() {
        return List.copyOf(estados);
    }

    /**
     * Busca un estado por su nombre.
     * Lanza excepción si no existe.
     */
    public Estado buscarPorNombre(String nombreEstado) throws OblException {
        if (nombreEstado == null || nombreEstado.isBlank()) {
            throw new OblException("El nombre del estado no puede estar vacío");
        }

        for (Estado estado : estados) {
            if (estado.nombre().equalsIgnoreCase(nombreEstado)) {
                return estado;
            }
        }

        throw new OblException("No existe el estado: " + nombreEstado);
    }
}
