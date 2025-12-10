package dao;

import database.IDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeliveryDAO {
    private IDatabase db;

    public DeliveryDAO(IDatabase db) { this.db = db; }

    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS delivery (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pedido_id INTEGER, " +
                "conductor_id INTEGER, " +
                "estado TEXT)";
        db.ejecutarConsulta(sql);
    }

    public void asignarDelivery(int idPedido, int idConductor) {
        String sql = "INSERT INTO delivery(pedido_id, conductor_id, estado) VALUES(?, ?, ?)";
        Connection conn = db.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);
            pstmt.setInt(2, idConductor);
            pstmt.setString(3, "EN CAMINO"); // Estado inicial
            pstmt.executeUpdate();
            System.out.println("Delivery asignado al conductor ID: " + idConductor + " para el Pedido: " + idPedido);
        } catch (Exception e) { e.printStackTrace(); }
        finally { db.desconectar(); }
    }
    // Método para consultar dónde está mi pedido
    public void verEstado(int idPedido) {
        String sql = "SELECT c.nombre, c.placa, d.estado FROM delivery d " +
                "JOIN conductores c ON d.conductor_id = c.id " +
                "WHERE d.pedido_id = " + idPedido;

        Connection conn = db.conectar();
        try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("\n--- ESTADO DEL PEDIDO #" + idPedido + " ---");
                System.out.println("Estado: " + rs.getString("estado"));
                System.out.println("Conductor: " + rs.getString("nombre"));
                System.out.println("Vehículo: " + rs.getString("placa"));
            } else {
                System.out.println("No existe delivery asignado para el pedido #" + idPedido);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { db.desconectar(); }
    }
}