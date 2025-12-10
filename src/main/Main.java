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
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // --- 1. CONFIGURACIÓN E INICIALIZACIÓN ---
        IDatabase db = new SQLiteDatabase();

        // Inicializar DAOs
        ProductoDAO productoDAO = new ProductoDAO(db);
        CategoriaDAO categoriaDAO = new CategoriaDAO(db);
        UsuarioDAO usuarioDAO = new UsuarioDAO(db);
        PedidoDAO pedidoDAO = new PedidoDAO(db);
        DeliveryDAO deliveryDAO = new DeliveryDAO(db);
        MetodoPagoDAO pagoDAO = new MetodoPagoDAO(db);
        ConductorDAO conductorDAO = new ConductorDAO(db);

        // Crear tablas (si no existen)
        categoriaDAO.crearTabla();
        productoDAO.crearTabla();
        usuarioDAO.crearTabla();
        pedidoDAO.crearTabla();
        deliveryDAO.crearTabla();
        pagoDAO.crearTabla();
        conductorDAO.crearTabla();

        // VERIFICACIÓN DE PRODUCTOS: Si hay pocos
        if (productoDAO.listar().size() < 2) {
            System.out.println("--- Restaurando Catálogo de Productos ---");
            // Nota: Asegúrate que los IDs de categoría (1 y 2) existan (Electrónica y Ropa)
            productoDAO.guardar(new Producto(0, "Laptop Gamer", 4500.0, 5, 1)); // Stock 5
            productoDAO.guardar(new Producto(0, "Mouse", 80.0, 10, 1));         // Stock 10
            productoDAO.guardar(new Producto(0, "Camiseta", 50.0, 20, 2));      // Stock 20
        }
        if (categoriaDAO.listar().isEmpty()) {
            System.out.println("--- Inicializando Base de Datos ---");
            categoriaDAO.guardar(new Categoria(0, "Electrónica"));
            categoriaDAO.guardar(new Categoria(0, "Ropa"));

            // También hacemos lo mismo con los pagos y conductores para que no se repitan
            pagoDAO.guardar(new MetodoPago(0, "Tarjeta de Crédito"));
            pagoDAO.guardar(new MetodoPago(0, "Yape / Plin"));
            pagoDAO.guardar(new MetodoPago(0, "Efectivo"));

            conductorDAO.guardar(new Conductor(0, "Juan Perez", "ABC-123", "DISPONIBLE"));
            conductorDAO.guardar(new Conductor(0, "Maria Lopez", "XYZ-987", "OCUPADO"));
        }
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println("===  SISTEMA E-COMMERCE INTERACTIVO ===");

        // --- 2. BUCLE DEL MENÚ ---
        while (opcion != 9) {
            System.out.println("\n---------------------------------------");
            System.out.println("       MENÚ PRINCIPAL");
            System.out.println("---------------------------------------");
            System.out.println("1. Registrar Nuevo Usuario");
            System.out.println("2. Agregar Nuevo Producto al Almacén");
            System.out.println("3. Eliminar un Producto");
            System.out.println("4. Ver Estado de mi Pedido (Tracking)");
            System.out.println("5. VER LISTA DE PRODUCTOS (Catálogo)");
            System.out.println("6. VER LISTA DE USUARIOS");
            System.out.println("7. REALIZAR UNA COMPRA (Carrito)");
            System.out.println("8. AGREGAR NUEVA CATEGORÍA"); // <--- NUEVA OPCIÓN
            System.out.println("9. Salir");
            System.out.print(" Ingresa el número de tu opción: ");
            try {
                String input = scanner.nextLine();
                // Validar que no esté vacío antes de parsear
                if (input.isEmpty()) continue;
                opcion = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("️ Por favor ingresa un número válido.");
                opcion = 0;
            }

            switch (opcion) {
                case 1: // CREAR USUARIO
                    System.out.print("Nombre: "); String nom = scanner.nextLine();
                    System.out.print("Correo: "); String mail = scanner.nextLine();
                    System.out.print("Password: "); String pass = scanner.nextLine();
                    System.out.print("Dirección: "); String dir = scanner.nextLine();
                    usuarioDAO.guardar(new Usuario(0, nom, mail, pass, dir));
                    break;

                case 2: // AGREGAR PRODUCTO (CON VALIDACIÓN)
                    System.out.print("Nombre: ");
                    String pNom = scanner.nextLine();

                    System.out.print("Precio: ");
                    double pPrecio = Double.parseDouble(scanner.nextLine());

                    System.out.print("Stock: ");
                    int pStock = Integer.parseInt(scanner.nextLine());

                    // --- VALIDACIÓN DE CATEGORÍA ---
                    System.out.println("\nCategorías disponibles:");
                    List<Categoria> listaCats = categoriaDAO.listar();

                    // 1. Mostramos las que existen
                    for (Categoria c : listaCats) {
                        System.out.println(c.getId() + ". " + c.getNombre());
                    }

                    System.out.print("Selecciona ID de Categoría: ");
                    int pCat = Integer.parseInt(scanner.nextLine());

                    // 2. Verificamos si el ID ingresado está en la lista
                    boolean categoriaExiste = false;
                    for (Categoria c : listaCats) {
                        if (c.getId() == pCat) {
                            categoriaExiste = true;
                            break;
                        }
                    }

                    if (categoriaExiste) {
                        productoDAO.guardar(new Producto(0, pNom, pPrecio, pStock, pCat));
                        System.out.println("Producto registrado correctamente.");
                    } else {
                        System.out.println("ERROR: La categoría ID " + pCat + " no existe. Producto NO guardado.");
                    }
                    break;

                case 3: // ELIMINAR
                    System.out.print("ID del producto a eliminar: ");
                    productoDAO.eliminar(Integer.parseInt(scanner.nextLine()));
                    break;

                case 4: // TRACKING
                    System.out.print("Ingresa el ID de tu Pedido: ");
                    deliveryDAO.verEstado(Integer.parseInt(scanner.nextLine()));
                    break;

                case 5: // LISTAR PRODUCTOS
                    System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");
                    List<Producto> productos = productoDAO.listar();
                    if(productos.isEmpty()) System.out.println("El inventario está vacío.");
                    for (Producto p : productos) {
                        System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " | S/." + p.getPrecio() + " | Stock: " + p.getStock());
                    }
                    break;

                case 6: // LISTAR USUARIOS (REAL)
                    System.out.println("\n---  LISTA DE USUARIOS REGISTRADOS ---");
                    List<Usuario> listaUsuarios = usuarioDAO.listar();

                    if (listaUsuarios.isEmpty()) {
                        System.out.println("️ No hay usuarios registrados aún.");
                    } else {
                        for (Usuario u : listaUsuarios) {
                            // Mostramos ID, Nombre y Correo
                            System.out.println("ID: [" + u.getId() + "] - " + u.getNombre() + " (" + u.getCorreo() + ")");
                        }
                    }
                    break;

                case 7: //  EL FLUJO DE COMPRA REAL
                    realizarCompra(scanner, usuarioDAO, productoDAO, pedidoDAO, deliveryDAO);
                    break;

                case 8: // NUEVA CATEGORÍA
                    System.out.print("Nombre de la nueva categoría: ");
                    String nuevaCat = scanner.nextLine();
                    // El ID es 0 porque es autoincremental
                    categoriaDAO.guardar(new Categoria(0, nuevaCat));
                    break;

                case 9: // SALIR (Antes era el 8)
                    System.out.println("Cerrando sistema...");
                    opcion = 9; // Forzamos la salida del bucle
                    break;
            }

        }
        db.desconectar();
    }

    // --- MÉTODO CON LA LÓGICA DEL CARRITO DE COMPRAS ---

    public static void realizarCompra(Scanner sc, UsuarioDAO uDAO, ProductoDAO pDAO, PedidoDAO pedDAO, DeliveryDAO delDAO) {
        System.out.println("\n--- INICIANDO PROCESO DE COMPRA ---");

        // 1. Identificarse
        System.out.print("Ingresa tu ID de Usuario: ");
        int idUser = Integer.parseInt(sc.nextLine());
        Usuario comprador = uDAO.obtenerPorId(idUser); // Necesitas asegurarte que este método exista en UsuarioDAO

        if (comprador == null) {
            System.out.println(" Usuario no encontrado. Regístrate primero.");
            return;
        }
        System.out.println("Hola, " + comprador.getNombre() + ". Vamos a comprar.");

        // 2. Crear el objeto Pedido en memoria
        PedidoOnline miPedido = new PedidoOnline(0, comprador, comprador.getDireccion(), "Tarjeta");

        // 3. Bucle para agregar productos
        while (true) {
            System.out.println("\n¿Qué deseas agregar? (Ingresa el ID del producto o '0' para pagar)");
            // Mostramos lista rápida
            List<Producto> lista = pDAO.listar();
            for (Producto p : lista) System.out.println("[" + p.getId() + "] " + p.getNombre() + " - S/." + p.getPrecio());

            System.out.print(" Tu elección: ");
            int idProd = Integer.parseInt(sc.nextLine());

            if (idProd == 0) break; // Salir del bucle

            // Buscar el producto real en la lista (manera simple)
            Producto seleccionado = null;
            for(Producto p : lista) {
                if(p.getId() == idProd) seleccionado = p;
            }

            if (seleccionado != null) {
                miPedido.agregarProducto(seleccionado);
                System.out.println("Agregado: " + seleccionado.getNombre());
            } else {
                System.out.println(" ID incorrecto.");
            }
        }

        // 4. Procesar y Pagar
        System.out.println("\n--- RESUMEN DE COMPRA ---");
        miPedido.procesarPedido(); // Esto imprime el total

        System.out.print("¿Confirmar compra? (S/N): ");
        String confirmar = sc.nextLine();

        if (confirmar.equalsIgnoreCase("S")) {
            // 1. Guardamos el pedido (Cabecera)
            int idPedidoGenerado = pedDAO.guardar(comprador.getId(), miPedido.getTotal());

            if (idPedidoGenerado != -1) {
                // 2. Asignar Delivery
                delDAO.asignarDelivery(idPedidoGenerado, 1);

                // 3. ACTUALIZAR EL STOCK
                System.out.println("--- Actualizando Inventario ---");
                for (Producto p : miPedido.getProductos()) {
                    pDAO.reducirStock(p.getId());
                }

                System.out.println("\n ¡COMPRA EXITOSA!");
                System.out.println("Guarda tu ID de Pedido para el tracking: #" + idPedidoGenerado);
            }
        } else {
            System.out.println("Compra cancelada.");
        }
    }
}