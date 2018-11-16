package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSource {
    private static DataSource INSTANCE;
    private ComboPooledDataSource pooledDataSource;

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("db");

        if (rb == null) {
            throw new DbManagerException("Бандл для db не был инициализирован.");
        } else {
            URL = rb.getString("url");
            USER = rb.getString("user");
            PASSWORD = rb.getString("password");
            DRIVER = rb.getString("driver");
        }
    }

    private DataSource() throws PropertyVetoException {
        pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setDriverClass(DRIVER);
        pooledDataSource.setJdbcUrl(URL);
        pooledDataSource.setUser(USER);
        pooledDataSource.setPassword(PASSWORD);

        //КАКИЕ НАСТРОЙКИ СТАВИТЬ??????
        pooledDataSource.setMinPoolSize(1);
        pooledDataSource.setAcquireIncrement(1);
        pooledDataSource.setMaxPoolSize(10);
        //pooledDataSource.setMaxStatements(180);
    }

    public static DataSource getInstance() throws PropertyVetoException {
        DataSource localInstance = INSTANCE;

        if (localInstance == null) {
            synchronized (DataSource.class) {
                localInstance = INSTANCE;

                if (localInstance == null) {
                    INSTANCE = localInstance = new DataSource();
                }
            }
        }

        return localInstance;
    }

    public Connection getConnection() throws SQLException {
        return pooledDataSource.getConnection();
    }
}
