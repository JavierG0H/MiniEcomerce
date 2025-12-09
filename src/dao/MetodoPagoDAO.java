package dao;

import database.IDatabase;
import model.MetodoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MetodoPagoDAO {
    private IDatabase db;

    public MetodoPagoDAO(IDatabase db) { this.db = db; }

    public void crearTabla() {
        // Tabla simple: ID y Nombre
        db.ejecutarConsulta("CREATE TABLE IF NOT EXISTS metodos_pago (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT)");
    }

    public void guardar(MetodoPago mp) {
        String sql = "INSERT INTO metodos_pago(nombre) VALUES(?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mp.getNombre());
            pstmt.executeUpdate();
            System.out.println("Método de Pago guardado: " + mp.getNombre());
        } catch (Exception e) { e.printStackTrace(); }
        finally { db.desconectar(); }
    }
}