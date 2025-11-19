package PedroWattimo.Obligatorio.models;

public class Categoria {
    private String nombre;

    public Categoria() {
    }

    /**
     * Valida los datos para crear una categoría.
     * Patrón Experto: la Categoría conoce sus reglas de validación.
     */
    public static void validarDatosCreacion(String nombre)
            throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException(
                    "El nombre de la categoría no puede estar vacío");
        }
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
