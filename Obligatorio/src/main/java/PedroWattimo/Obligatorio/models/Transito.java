package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;

public class Transito {
    private LocalDateTime fechaHora;
    private Puesto puesto;
    private Vehiculo vehiculo;
    private String categoriaVehiculo;
    private double costoBase;
    private double costoConTarifa;
    private String nombreBonificacion;
    private double porcentajeBonificacion;
    private double montoBonificacion;
    private double totalPagado;

    public Transito(LocalDateTime fechaHora,
            Puesto puesto,
            Vehiculo vehiculo,
            double costoBase,
            double costoConTarifa,
            String nombreBonificacion,
            double porcentajeBonificacion) {

        this.fechaHora = fechaHora;
        this.puesto = puesto;
        this.vehiculo = vehiculo;
        this.categoriaVehiculo = vehiculo.getCategoria().getNombre();
        this.costoBase = costoBase;
        this.costoConTarifa = costoConTarifa;
        this.nombreBonificacion = nombreBonificacion;
        this.porcentajeBonificacion = porcentajeBonificacion < 0 ? 0 : porcentajeBonificacion;
        this.montoBonificacion = this.costoConTarifa * (this.porcentajeBonificacion / 100.0);
        this.totalPagado = this.costoConTarifa - this.montoBonificacion;
    }

    public LocalDateTime fechaHora() {
        return fechaHora;
    }

    public Puesto puesto() {
        return puesto;
    }

    public Vehiculo vehiculo() {
        return vehiculo;
    }

    public String categoriaVehiculo() {
        return categoriaVehiculo;
    }

    public double costoBase() {
        return costoBase;
    }

    public double costoConTarifa() {
        return costoConTarifa;
    }

    public String nombreBonificacion() {
        return nombreBonificacion;
    }

    public double porcentajeBonificacion() {
        return porcentajeBonificacion;
    }

    public double montoBonificacion() {
        return montoBonificacion;
    }

    public double totalPagado() {
        return totalPagado;
    }
}
