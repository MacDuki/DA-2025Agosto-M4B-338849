package PedroWattimo.Obligatorio.dtos;

public class RespuestaVista {
    private String id;
    private Object parametro;

    public RespuestaVista() {
    }

    public RespuestaVista(String id, Object parametro) {
        this.id = id;
        this.parametro = parametro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getParametro() {
        return parametro;
    }

    public void setParametro(Object parametro) {
        this.parametro = parametro;
    }
}
