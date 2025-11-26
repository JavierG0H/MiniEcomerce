package model;

import java.util.ArrayList;

public abstract class Pedido {
    protected int id;
    protected Cliente cliente;
    protected ArrayList<Producto> productos;
    protected double total;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        total += producto.getPrecio();
    }

    public double getTotal() { return total; }

    public Cliente getCliente() { return cliente; }

    public ArrayList<Producto> getProductos() { return productos; }

    // Método abstracto (las subclases deben implementarlo)
    public abstract void procesarPedido();

    @Override
    public String toString() {
        return "Pedido #" + id + " - Cliente: " + cliente.getNombre() + " - Total: $" + total;
    }
}