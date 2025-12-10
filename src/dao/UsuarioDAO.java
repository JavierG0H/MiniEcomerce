package dao;

import database.IDatabase;
import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private IDatabase db;

    public UsuarioDAO(IDatabase db) { this.db = db; }

    public void crearTabla() {
        // Creamos la tabla 'usuarios' como pidió la docente
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "correo TEXT, " +
                "password TEXT, " +
                "direccion TEXT)";
        db.ejecutarConsulta(sql);
    }

    public void guardar(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, correo, password, direccion) VALUES(?, ?, ?, ?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getCorreo());
            pstmt.setString(3, u.getPassword());
            pstmt.setString(4, u.getDireccion());
            pstmt.executeUpdate();
            System.out.println("Usuario guardado: " + u.getNombre());
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    // Método extra para buscar usuario por ID (nos servirá para los Pedidos)
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = " + id;
        Connection conn = db.conectar();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("direccion")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { db.desconectar(); }
        return null;
    }
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        Connection conn = db.conectar();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("direccion")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }
}