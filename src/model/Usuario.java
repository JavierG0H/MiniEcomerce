package model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password; // Nuevo campo típico de usuario
    private String direccion;

    public Usuario(int id, String nombre, String correo, String password, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getDireccion() { return direccion; }

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }
}