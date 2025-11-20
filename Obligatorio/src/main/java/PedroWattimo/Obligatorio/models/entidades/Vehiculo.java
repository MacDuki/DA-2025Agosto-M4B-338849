package PedroWattimo.Obligatorio.models.entidades;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private String color;
    private Categoria categoria;
    private Propietario propietario;

    public Vehiculo() {
    }

    public static void validarDatosCreacion(String matricula, String modelo, String nombreCategoria)
            throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        if (matricula == null || matricula.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La matrícula no puede estar vacía");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("El modelo no puede estar vacío");
        }
        if (nombreCategoria == null || nombreCategoria.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La categoría no puede estar vacía");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vehiculo v))
            return false;
        return matricula.equals(v.matricula);
    }

}
