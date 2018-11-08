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
    private static ResourceBundle rb = null;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static Connection cn;

    static {
        rb = ResourceBundle.getBundle("db");

        if (rb == null) {
            URL = "UNDEFINED";
            USER = "UNDEFINED";
            PASSWORD = "UNDEFINED";
            throw new DbManagerException("Бандл для db не был инициализирован.");
        } else {
            URL = rb.getString("url");
            USER = rb.getString("user");
            PASSWORD = rb.getString("password");
        }

        try {
            Class.forName(rb.getString("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (cn == null) {
                cn = DriverManager.getConnection(URL, USER, PASSWORD);
            }

            return cn;
        } catch (SQLException e) {
            throw new DbManagerException("Ошибка соединения " + e.getMessage());
        }
    }
}
