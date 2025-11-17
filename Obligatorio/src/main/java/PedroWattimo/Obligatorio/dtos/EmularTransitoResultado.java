package PedroWattimo.Obligatorio.dtos;

public class EmularTransitoResultado {
    private String nombrePropietario;
    private String estadoPropietario;
    private String categoriaVehiculo;
    private String bonificacionNombre; // null si no aplica
    private double costoTransito; // monto base (tarifa)
    private double montoBonificacion; // descuento aplicado
    private double montoPagado; // monto efectivamente pagado
    private int saldoPost; // saldo del propietario después del débito

    public EmularTransitoResultado() {
    }

    public EmularTransitoResultado(String nombrePropietario, String estadoPropietario,
            String categoriaVehiculo, String bonificacionNombre,
            double costoTransito, double montoBonificacion,
            double montoPagado, int saldoPost) {
        this.nombrePropietario = nombrePropietario;
        this.estadoPropietario = estadoPropietario;
        this.categoriaVehiculo = categoriaVehiculo;
        this.bonificacionNombre = bonificacionNombre;
        this.costoTransito = costoTransito;
        this.montoBonificacion = montoBonificacion;
        this.montoPagado = montoPagado;
        this.saldoPost = saldoPost;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getEstadoPropietario() {
        return estadoPropietario;
    }

    public void setEstadoPropietario(String estadoPropietario) {
        this.estadoPropietario = estadoPropietario;
    }

    public String getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public void setCategoriaVehiculo(String categoriaVehiculo) {
        this.categoriaVehiculo = categoriaVehiculo;
    }

    public String getBonificacionNombre() {
        return bonificacionNombre;
    }

    public void setBonificacionNombre(String bonificacionNombre) {
        this.bonificacionNombre = bonificacionNombre;
    }

    public double getCostoTransito() {
        return costoTransito;
    }

    public void setCostoTransito(double costoTransito) {
        this.costoTransito = costoTransito;
    }

    public double getMontoBonificacion() {
        return montoBonificacion;
    }

    public void setMontoBonificacion(double montoBonificacion) {
        this.montoBonificacion = montoBonificacion;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public int getSaldoPost() {
        return saldoPost;
    }

    public void setSaldoPost(int saldoPost) {
        this.saldoPost = saldoPost;
    }
}
