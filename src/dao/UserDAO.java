package dao;

import com.google.gson.JsonObject;
import entites.User;

import java.sql.SQLException;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
public interface UserDAO extends DAO<User> {
    String getUserAvatar(long userID) throws SQLException;

    String getUserName(long userID) throws SQLException;

    JsonObject getUserRoleAndStatus(long userID) throws SQLException;

    int restoreUser(long userID) throws SQLException;

    int assignAdmin(long userID) throws SQLException;

    int assignUser(long userID) throws SQLException;
}
