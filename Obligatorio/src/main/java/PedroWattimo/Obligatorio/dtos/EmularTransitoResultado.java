package PedroWattimo.Obligatorio.dtos;

import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Transito;

public class EmularTransitoResultado {
    private String nombrePropietario;
    private String estadoPropietario;
    private String categoriaVehiculo;
    private String bonificacionNombre;
    private double costoTransito;
    private double montoBonificacion;
    private double montoPagado;
    private int saldoPost;

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

    public EmularTransitoResultado(Transito t, Propietario p) {
        this.nombrePropietario = p.getNombreCompleto();
        this.estadoPropietario = p.getEstadoActual() != null ? p.getEstadoActual().nombre() : "HABILITADO";
        this.categoriaVehiculo = t.categoriaVehiculo();
        this.bonificacionNombre = t.nombreBonificacion();
        this.costoTransito = t.costoConTarifa();
        this.montoBonificacion = t.montoBonificacion();
        this.montoPagado = t.totalPagado();
        this.saldoPost = p.getSaldoActual();
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
