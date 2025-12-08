package model;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private int idCategoria;

    public Producto(int id, String nombre, double precio, int stock, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria= idCategoria;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public int getIdCategoria() { return idCategoria; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre + " - S/." + precio + " (Stock: " + stock + ") [Cat ID: " + idCategoria+ "]";
    }
}

