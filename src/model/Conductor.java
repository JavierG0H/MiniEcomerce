package model;

public class Conductor {
    private int id;
    private String nombre;
    private String placa;
    private String estado; // "DISPONIBLE" o "OCUPADO"

    public Conductor(int id, String nombre, String placa, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.placa = placa;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPlaca() { return placa; }
    public String getEstado() { return estado; }
}