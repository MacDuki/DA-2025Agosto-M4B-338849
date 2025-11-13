package PedroWattimo.Obligatorio.models;

public class Estado {

    private String nombre; // "Habilitado", "Penalizado", "Suspendido", "Deshabilitado"

    public static Estado HABILITADO = new Estado("Habilitado");
    public static Estado PENALIZADO = new Estado("Penalizado");
    public static Estado SUSPENDIDO = new Estado("Suspendido");
    public static Estado DESHABILITADO = new Estado("Deshabilitado");

    private Estado(String nombre) {
        this.nombre = nombre;
    }

    public String nombre() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Patrón Experto: el Estado sabe si permite transitar.
     * Reglas:
     * - Habilitado: puede transitar
     * - Penalizado: puede transitar (pero sin bonificaciones ni notificaciones)
     * - Suspendido: NO puede transitar
     * - Deshabilitado: NO puede transitar
     */
    public boolean permiteTransitar() {
        return this.equals(HABILITADO) || this.equals(PENALIZADO);
    }

    /**
     * Patrón Experto: el Estado sabe si permite aplicar bonificaciones.
     * Solo Penalizado NO permite bonificaciones.
     */
    public boolean permiteBonificaciones() {
        return !this.equals(PENALIZADO);
    }

    /**
     * Patrón Experto: el Estado sabe si permite registrar notificaciones.
     * Solo Penalizado NO permite notificaciones.
     */
    public boolean permiteNotificaciones() {
        return !this.equals(PENALIZADO);
    }

    /**
     * Patrón Experto: el Estado sabe si permite ingresar al sistema.
     * Solo Deshabilitado NO permite ingresar.
     */
    public boolean permiteIngresar() {
        return !this.equals(DESHABILITADO);
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
