package PedroWattimo.Obligatorio.dtos;

public class EmularTransitoRequest {
    private Long puestoId;
    private String matricula;
    private String fechaHora; // ISO-8601 format

    public EmularTransitoRequest() {
    }

    public EmularTransitoRequest(Long puestoId, String matricula, String fechaHora) {
        this.puestoId = puestoId;
        this.matricula = matricula;
        this.fechaHora = fechaHora;
    }

    public Long getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(Long puestoId) {
        this.puestoId = puestoId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
