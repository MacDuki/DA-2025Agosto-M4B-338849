package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private String color;
    private Categoria categoria;
    private Propietario propietario;
    private List<Transito> transitos;

    public Vehiculo() {
    }

    public Vehiculo(String matricula, String modelo, String color, Categoria categoria, Propietario propietario) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.categoria = categoria;
        this.propietario = propietario;
        this.transitos = new ArrayList<Transito>();

    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    // Encapsulamiento: retornar copias inmutables para prevenir modificaciones
    // externas
    public List<Transito> getTransitos() {
        return transitos == null ? List.of() : List.copyOf(transitos);
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    /**
     * Patrón Experto: el Vehiculo sabe si pertenece a una categoría específica.
     */
    public boolean esDeCategoria(Categoria c) {
        if (c == null || this.categoria == null)
            return false;
        return this.categoria.equals(c) ||
                (this.categoria.getNombre() != null && this.categoria.getNombre().equals(c.getNombre()));
    }

    public int cantidadTransitos() {
        return transitos == null ? 0 : transitos.size();
    }

    public double totalGastadoPorMi() {
        if (transitos == null)
            return 0.0;
        return transitos.stream().mapToDouble(Transito::totalPagado).sum();
    }

    /**
     * Patrón Experto: el Vehiculo mantiene su lista de tránsitos.
     */
    public void registrarTransito(Transito transito) {
        if (transito == null)
            return;
        if (this.transitos == null)
            this.transitos = new ArrayList<>();
        this.transitos.add(transito);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vehiculo v))
            return false;
        return matricula.equals(v.matricula);
    }

}
