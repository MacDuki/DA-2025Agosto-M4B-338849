package PedroWattimo.Obligatorio.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Usuario {
    protected int cedula;
    protected String nombreCompleto;
    protected String contraseña;

    public Usuario() {
    }

    public Usuario(int cedula, String nombreCompleto, String contraseñaPlana) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.contraseña = hash(contraseñaPlana);
    }

    public int getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean passwordCorrecta(String pwd) {
        if (pwd == null || pwd.isBlank() || this.contraseña == null || this.contraseña.isBlank()) {
            return false;
        }
        return this.contraseña.equals(hash(pwd));
    }

    protected String hash(String input) {
        if (input == null)
            return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    protected String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
