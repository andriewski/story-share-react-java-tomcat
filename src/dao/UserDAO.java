package dao;

import entites.User;

import java.sql.SQLException;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
public interface UserDAO extends DAO<User> {
    String getUserAvatar(long userID) throws SQLException;
}
