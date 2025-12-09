package model;

public class PedidoOnline extends Pedido {
    private String direccionEnvio;
    private String metodoPago;

    public PedidoOnline(int id, Usuario cliente, String direccionEnvio, String metodoPago) {
        super(id, cliente);
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    @Override
    public void procesarPedido() {
        System.out.println("Procesando pedido online #" + id + " para " + cliente.getNombre());
        System.out.println("Dirección: " + direccionEnvio);
        System.out.println("Método de pago: " + metodoPago);
        System.out.println("Total: S/." + total);
    }
}