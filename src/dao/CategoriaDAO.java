package dao;

import database.IDatabase;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private IDatabase db;

    public CategoriaDAO(IDatabase db) {
        this.db = db;
    }

    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS categorias (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL)";
        db.ejecutarConsulta(sql);
    }

    public void guardar(Categoria c) {
        String sql = "INSERT INTO categorias(nombre) VALUES(?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getNombre());
            pstmt.executeUpdate();
            System.out.println("Categoría guardada: " + c.getNombre());
        } catch (Exception e) {
            System.err.println("Error al guardar categoría: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        Connection conn = db.conectar();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.desconectar();
        }
        return lista;
    }
}