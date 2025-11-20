package PedroWattimo.Obligatorio.models.subsistemas;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.fabricas.FabricaEstados;

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
        return FabricaEstados.crear(nombreEstado);
    }

}
