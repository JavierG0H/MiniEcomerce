package database;
import java.sql.Connection;

public interface IDatabase {
    Connection conectar();

    void desconectar();

    void ejecutarConsulta(String sql);

}
