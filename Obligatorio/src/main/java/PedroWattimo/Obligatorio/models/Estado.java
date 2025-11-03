package PedroWattimo.Obligatorio.models;

public class Estado {

    private String nombre; // "Habilitado", "Penalizado", "Suspendido"

    public static Estado HABILITADO = new Estado("Habilitado");
    public static Estado PENALIZADO = new Estado("Penalizado");
    public static Estado SUSPENDIDO = new Estado("Suspendido");

    private Estado(String nombre) {
        this.nombre = nombre;
    }

    public String nombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Estado e))
            return false;
        return nombre.equals(e.nombre);
    }

}
