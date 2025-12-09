package dao;

import database.IDatabase;
import model.Conductor;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConductorDAO {
    private IDatabase db;

    public ConductorDAO(IDatabase db) { this.db = db; }

    public void crearTabla() {
        db.ejecutarConsulta("CREATE TABLE IF NOT EXISTS conductores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "placa TEXT, " +
                "estado TEXT)");
    }

    public void guardar(Conductor c) {
        String sql = "INSERT INTO conductores(nombre, placa, estado) VALUES(?, ?, ?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getNombre());
            pstmt.setString(2, c.getPlaca());
            pstmt.setString(3, c.getEstado());
            pstmt.executeUpdate();
            System.out.println("Conductor guardado: " + c.getNombre());
        } catch (Exception e) { e.printStackTrace(); }
        finally { db.desconectar(); }
    }
}