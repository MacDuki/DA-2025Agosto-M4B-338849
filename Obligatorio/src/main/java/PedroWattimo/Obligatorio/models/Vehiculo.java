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

    public List<Transito> getTransitos() {
        return transitos;
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

    // Expert helpers
    public int cantidadTransitos() {
        return transitos == null ? 0 : transitos.size();
    }

    public double totalGastadoPorMi() {
        if (transitos == null)
            return 0.0;
        return transitos.stream().mapToDouble(Transito::totalPagado).sum();
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
