package PedroWattimo.Obligatorio.models.subsistemas;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.fabricas.FabricaEstados;

/**
 * FUENTE DE VERDAD: La lista interna 'estados' es la única fuente de verdad.
 * La fábrica (FabricaEstados) se usa únicamente para crear las instancias
 * únicas
 * durante la inicialización del sistema. Todas las búsquedas retornan objetos
 * de la lista, garantizando identidad compartida (singleton por tipo de
 * estado).
 */
public class SistemaEstados {

    private final List<Estado> estados;

    protected SistemaEstados() {
        this.estados = new ArrayList<>();

        this.estados.add(FabricaEstados.crearHabilitado());
        this.estados.add(FabricaEstados.crearPenalizado());
        this.estados.add(FabricaEstados.crearSuspendido());
        this.estados.add(FabricaEstados.crearDeshabilitado());
    }

    public List<Estado> listarEstados() {
        return List.copyOf(estados);
    }

    public Estado buscarPorNombre(String nombreEstado) throws OblException {
        if (nombreEstado == null || nombreEstado.isBlank()) {
            throw new OblException("El nombre del estado no puede estar vacío");
        }

        return estados.stream()
                .filter(e -> nombreEstado.equalsIgnoreCase(e.getNombre()))
                .findFirst()
                .orElseThrow(() -> new OblException("Tipo de estado desconocido: " + nombreEstado));
    }

}
