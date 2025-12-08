package dao;

import database.IDatabase;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private IDatabase db;

    public ProductoDAO(IDatabase db) {
        this.db = db;
    }

    public void crearTabla() {
        // Categoria

        String sql = "CREATE TABLE IF NOT EXISTS productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "precio REAL," +
                "stock INTEGER," +
                "categoria_id INTEGER)"; // Esta es la Llave Foránea
        db.ejecutarConsulta(sql);
    }

    public void guardar(Producto producto) {
        // Insert
        String sql = "INSERT INTO productos(nombre, precio, stock, categoria_id) VALUES(?, ?, ?, ?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setInt(3, producto.getStock());
            pstmt.setInt(4, producto.getIdCategoria()); // Guardamos el ID de la categoría
            pstmt.executeUpdate();
            System.out.println(" Producto guardado: " + producto.getNombre());
        } catch (Exception e) {
            System.out.println(" Error al guardar: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        Connection conn = db.conectar();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                //Leemos la columna categoria_id y la pasamos al constructor
                lista.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id") // Recuperamos la relación
                ));
            }
        } catch (Exception e) {
            System.out.println("Error al listar: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }
}