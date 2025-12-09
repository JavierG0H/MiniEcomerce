
package dao;

import database.IDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PedidoDAO {
    private IDatabase db;

    public PedidoDAO(IDatabase db) { this.db = db; }

    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER, " +
                "total REAL, " +
                "fecha TEXT)";
        db.ejecutarConsulta(sql);
    }

    public int guardar(int idUsuario, double total) {
        Connection conn = db.conectar();

        // Validación de seguridad para la demo
        if (idUsuario == 0) idUsuario = 1;

        String sql = "INSERT INTO pedidos(usuario_id, total, fecha) VALUES(?, ?, date('now'))";
        int idGenerado = -1;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUsuario);
            pstmt.setDouble(2, total);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Recuperar ID de forma segura
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");

                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                    System.out.println("Pedido registrado con Éxito (ID: " + idGenerado + ")");
                }
                rs.close();
                stmt.close();
            }
            pstmt.close();
        } catch (Exception e) {
            System.err.println("Error al guardar pedido: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.desconectar();
        }

        return idGenerado;
    }
}