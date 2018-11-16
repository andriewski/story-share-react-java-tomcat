package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
public class ConnectionManager {
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    public static Connection getConnection() throws DbManagerException {
        try {
            if (tl.get() == null) {
                tl.set(DataSource.getInstance().getConnection());
            }

            return tl.get();
        } catch (Exception e) {
            throw new DbManagerException("Ошибка получения соединения " +  e.getMessage());
        }
    }
}
