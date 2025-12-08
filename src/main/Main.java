package main;

import model.Categoria;
import dao.CategoriaDAO;
import dao.ProductoDAO;
import database.IDatabase;
import database.SQLiteDatabase;
import model.Cliente;
import model.PedidoOnline;
import model.Producto;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA E-COMMERCE ===\n");

        // Confi de la BD

        IDatabase db = new SQLiteDatabase();
        ProductoDAO productoDAO = new ProductoDAO(db);

        // Verifcando existencia de la tabla

        productoDAO.crearTabla();

        CategoriaDAO categoriaDAO = new CategoriaDAO(db);
        categoriaDAO.crearTabla();

        // Crear y guardar categorías de prueba
        System.out.println("--- Guardando Categorías ---");
        categoriaDAO.guardar(new Categoria(0, "Electrónica"));
        categoriaDAO.guardar(new Categoria(0, "Ropa"));

        // Creando objeto

        Cliente c1 = new Cliente(1, "Javier Escobedo", "javier15@gmail.com");

        Producto p1 = new Producto(0, "Laptop", 2500.0, 5, 1);
        Producto p2 = new Producto(0, "Mouse", 80.0, 10, 1);
        Producto p3 = new Producto(0, "Camiseta", 50.0, 20,2);

        // Guardado en BD

        System.out.println("--- Guardando productos en Inventario (BD) ---");
        productoDAO.guardar(p1);
        productoDAO.guardar(p2);
        productoDAO.guardar(p3);
        System.out.println("----------------------------------------------\n");


        // 4. Porcedimiento de venta

        System.out.println("--- Procesando Venta ---");
        PedidoOnline pedido = new PedidoOnline(1001, c1, "Av. Los Olivos 123", "Tarjeta de crédito");

        // Agregamos los productos al carrito
        pedido.agregarProducto(p1);
        pedido.agregarProducto(p2);

        // Mostramos el ticket final
        pedido.procesarPedido();

        // Cerramos la conexión al terminar
        db.desconectar();
    }
}