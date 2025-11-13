package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para la solicitud de asignación de bonificación.
 */
public class AsignarBonificacionRequest {
    private String cedula;
    private String nombreBonificacion;
    private String nombrePuesto;

    public AsignarBonificacionRequest() {
    }

    public AsignarBonificacionRequest(String cedula, String nombreBonificacion, String nombrePuesto) {
        this.cedula = cedula;
        this.nombreBonificacion = nombreBonificacion;
        this.nombrePuesto = nombrePuesto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreBonificacion() {
        return nombreBonificacion;
    }

    public void setNombreBonificacion(String nombreBonificacion) {
        this.nombreBonificacion = nombreBonificacion;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }
}
