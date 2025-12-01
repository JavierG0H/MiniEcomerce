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

            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(URL);
            System.out.println(" Conexión a SQLite establecida.");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println(" Error CRÍTICO: No se encontró la clase del Driver de SQLite. Asegúrate de tener el .jar bien puesto.");
            return null;
        } catch (SQLException e) {
            System.err.println(" Error al conectar a la URL: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ejecutarConsulta(String sql) {
        if (connection == null) conectar();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}