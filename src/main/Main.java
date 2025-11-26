package main;

import model.*;

public class Main {
    public static void main(String[] args) {
        Cliente c1 = new Cliente(1, "Javier Escobedo", "javier15@gmail.com");

        Producto p1 = new Producto(1, "Laptop", 2500.0, 5);
        Producto p2 = new Producto(2, "Mouse", 80.0, 10);

        PedidoOnline pedido = new PedidoOnline(1001, c1, "Av. Los Olivos 123", "Tarjeta de crédito");
        pedido.agregarProducto(p1);
        pedido.agregarProducto(p2);

        pedido.procesarPedido();
    }
}