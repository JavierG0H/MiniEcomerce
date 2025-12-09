package main;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import database.IDatabase;
import database.SQLiteDatabase;
import model.Categoria;
import model.PedidoOnline;
import model.Producto;
import model.Usuario;
import dao.MetodoPagoDAO;
import dao.ConductorDAO;
import model.MetodoPago;
import model.Conductor;
import dao.DeliveryDAO;
import dao.PedidoDAO;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA E-COMMERCE ===\n");

        // 1. Configuración de la BD

        IDatabase db = new SQLiteDatabase();

        // Inicializamos los DAOs
        ProductoDAO productoDAO = new ProductoDAO(db);
        CategoriaDAO categoriaDAO = new CategoriaDAO(db);
        UsuarioDAO usuarioDAO = new UsuarioDAO(db);

        // 2. Verificar existencia de tablas (Orden recomendado)

        categoriaDAO.crearTabla();
        productoDAO.crearTabla();
        usuarioDAO.crearTabla();

        // 3. Crear y guardar categorías de prueba

        System.out.println("--- Guardando Categorías ---");
        categoriaDAO.guardar(new Categoria(0, "Electrónica"));
        categoriaDAO.guardar(new Categoria(0, "Ropa"));

        // 4. Crear Productos (Asignando ID de categoría)

        Producto p1 = new Producto(0, "Laptop", 2500.0, 5, 1);
        Producto p2 = new Producto(0, "Mouse", 80.0, 10, 1);
        Producto p3 = new Producto(0, "Camiseta", 50.0, 20, 2);

        // 5. Guardado de productos en BD

        System.out.println("\n--- Guardando productos en Inventario (BD) ---");
        productoDAO.guardar(p1);
        productoDAO.guardar(p2);
        productoDAO.guardar(p3);
        System.out.println("----------------------------------------------");

        // 6. Crear y Guardar Usuario (Aquí usamos el constructor nuevo de 5 datos)

        System.out.println("\n--- Guardando Usuario ---");
        Usuario user = new Usuario(0, "Javier Escobedo", "javier@email.com", "12345", "Av. Los Olivos 123");
        usuarioDAO.guardar(user);

        // 8. GESTIÓN DE MÉTODOS DE PAGO

        MetodoPagoDAO pagoDAO = new MetodoPagoDAO(db);
        pagoDAO.crearTabla();
        System.out.println("\n--- Guardando Métodos de Pago ---");
        pagoDAO.guardar(new MetodoPago(0, "Tarjeta de Crédito"));
        pagoDAO.guardar(new MetodoPago(0, "Yape / Plin"));
        pagoDAO.guardar(new MetodoPago(0, "Efectivo contraentrega"));

        // 9. GESTIÓN DE CONDUCTORES
        ConductorDAO conductorDAO = new ConductorDAO(db);
        conductorDAO.crearTabla();
        System.out.println("\n--- Guardando Conductores ---");
        conductorDAO.guardar(new Conductor(0, "Juan Perez", "ABC-123", "DISPONIBLE"));
        conductorDAO.guardar(new Conductor(0, "Maria Lopez", "XYZ-987", "OCUPADO"));

        // 10. Procesar Venta

        System.out.println("\n--- Procesando Venta ---");
        PedidoOnline pedido = new PedidoOnline(1001, user, user.getDireccion(), "Tarjeta");

        // Agregamos los productos al carrito

        pedido.agregarProducto(p1);
        pedido.agregarProducto(p2);

        // Mostramos el ticket final

        pedido.procesarPedido();

        // 11. GUARDAR EL PEDIDO EN BD
        System.out.println("\n--- Registrando Pedido y Delivery en BD ---");

        PedidoDAO pedidoDAO = new PedidoDAO(db);
        pedidoDAO.crearTabla();

        // Guardamos el pedido (Usuario ID 1, Total 2580.0)
        int idPedidoGuardado = pedidoDAO.guardar(user.getId(), 2580.0);

        // 12. ASIGNAR DELIVERY AUTOMÁTICO
        DeliveryDAO deliveryDAO = new DeliveryDAO(db);
        deliveryDAO.crearTabla();

        if (idPedidoGuardado != -1) {
            // Asignamos al conductor #1 (Juan Perez)
            deliveryDAO.asignarDelivery(idPedidoGuardado, 1);
        } else {
            System.err.println("Error: No se pudo generar el pedido.");
        }

        // Cerramos la conexión al terminar
        db.desconectar();
    }
}