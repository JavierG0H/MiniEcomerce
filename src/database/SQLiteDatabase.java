package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase implements IDatabase {
    private Connection connection;
    private static final String URL = "jdbc:sqlite:ecommerce.db";

    @Override
    public Connection conectar() {
        try {
            // Verificación simple y directa
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            return connection;

        } catch (Exception e) {
            System.err.println("Error CRÍTICO de conexión: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ejecutarConsulta(String sql) {
        try {

            if (connection == null || connection.isClosed()) {
                conectar();
            }

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Error ejecutando consulta SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
