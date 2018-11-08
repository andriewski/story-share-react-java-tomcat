package dao;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
public interface DAO<T> {
    T save(T t) throws SQLException;
    T get(Serializable t) throws SQLException;
    void update(T t) throws SQLException;
    int delete(Serializable id) throws SQLException;
}
