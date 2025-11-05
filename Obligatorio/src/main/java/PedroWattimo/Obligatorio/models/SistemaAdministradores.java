package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaAdministradores {
    private List<Administrador> administradores = new ArrayList<>();

    protected SistemaAdministradores() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Administrador> obtenerAdministradoresInternos() {
        return administradores;
    }
}
