package model;
import java.util.ArrayList;
public abstract class Pedido {
    protected int id;
    protected Usuario cliente;
    protected ArrayList<Producto> productos;
    protected double total;

    public Pedido(int id, Usuario cliente) {
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

    public Usuario getCliente() { return cliente; }

    public ArrayList<Producto> getProductos() { return productos; }


    public abstract void procesarPedido();

    @Override
    public String toString() {
        return "Pedido #" + id + " - Cliente: " + cliente.getNombre() + " - Total: $" + total;
    }
}