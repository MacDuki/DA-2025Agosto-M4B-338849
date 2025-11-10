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
    private double montoBonificacion;

    private double porcentajeBonificacion;
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

    public Transito(Puesto puesto, Vehiculo vehiculo, Tarifa tarifa,
            double montoBonificacion, double montoPagado,
            LocalDateTime fechaHora, Bonificacion bonificacionAplicada) {
        this.fechaHora = fechaHora;
        this.puesto = puesto;
        this.vehiculo = vehiculo;
        this.categoriaVehiculo = vehiculo != null && vehiculo.getCategoria() != null
                ? vehiculo.getCategoria().getNombre()
                : "Desconocida";
        this.costoBase = tarifa != null ? tarifa.getMonto() : 0.0;
        this.costoConTarifa = this.costoBase;
        this.nombreBonificacion = bonificacionAplicada != null ? bonificacionAplicada.getNombre() : null;
        this.montoBonificacion = Math.max(0, montoBonificacion);
        this.porcentajeBonificacion = this.costoBase > 0
                ? (this.montoBonificacion / this.costoBase) * 100.0
                : 0.0;
        this.totalPagado = Math.max(0, montoPagado);
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
