package PedroWattimo.Obligatorio.models;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private String color;
    private Categoria categoria;
    private Propietario propietario;

    public Vehiculo() {
    }

    public Vehiculo(String matricula, String modelo, String color, Categoria categoria, Propietario propietario) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.categoria = categoria;
        this.propietario = propietario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Propietario getPropietario() {
        return propietario;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vehiculo v))
            return false;
        return matricula.equals(v.matricula);
    }

}
