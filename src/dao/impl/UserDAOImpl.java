package dao.impl;

import dao.UserDAO;
import db.ConnectionManager;
import entites.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */

public class UserDAOImpl implements UserDAO {
    private static volatile UserDAO INSTANCE = null;
    private static final String saveUserQuery = "INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD) VALUES (?, ?, ?, ?)";
    private static final String getUserQuery = "SELECT * FROM USERS WHERE EMAIL=?";
    private static final String updateUserQuery = "UPDATE USERS SET NAME=?, EMAIL=?, AVATAR=?, PASSWORD=? " +
            "WHERE USER_ID=?";
    private static final String deleteUserQuery = "UPDATE USERS SET DELETED = '+' WHERE USER_ID=?";
    private static final String getUserAvatar = "SELECT AVATAR FROM USERS WHERE USER_ID = ?";

    private PreparedStatement psSave;
    private PreparedStatement psGet;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psGetUserAvatar;
    private PreparedStatement psGetUsersAvatars;

    {
        try {
            psSave = ConnectionManager.getConnection().prepareStatement(saveUserQuery, Statement.RETURN_GENERATED_KEYS);
            psGet = ConnectionManager.getConnection().prepareStatement(getUserQuery);
            psUpdate = ConnectionManager.getConnection().prepareStatement(updateUserQuery);
            psDelete = ConnectionManager.getConnection().prepareStatement(deleteUserQuery);
            psGetUserAvatar = ConnectionManager.getConnection().prepareStatement(getUserAvatar);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private UserDAOImpl() {
    }

    public static UserDAO getInstance() {
        UserDAO localInstance = INSTANCE;

        if (localInstance == null) {
            synchronized (UserDAOImpl.class) {
                localInstance = INSTANCE;

                if (localInstance == null) {
                    INSTANCE = localInstance = new UserDAOImpl();
                }
            }
        }

        return localInstance;
    }

    @Override
    public User save(User user) throws SQLException {
        psSave.setString(1, user.getName());
        psSave.setString(2, user.getEmail());
        psSave.setString(3, user.getAvatar());
        psSave.setString(4, user.getPassword());
        psSave.executeUpdate();
        ResultSet rs = psSave.getGeneratedKeys();

        if (rs.next()) { //Узнаем наш id в базе данных
            user.setId(rs.getLong(1));
        }

        close(rs);

        return user;
    }

    @Override
    public User get(Serializable t) throws SQLException {
        psGet.setString(1, t.toString());
        ResultSet rs = psGet.executeQuery();
        User user = null;

        if (rs.next()) {
            user = new User(rs.getLong(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6).equals("+"));
        }

        close(rs);

        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        psUpdate.setString(1, user.getName());
        psUpdate.setString(2, user.getEmail());
        psUpdate.setString(3, user.getAvatar());
        psUpdate.setString(4, user.getPassword());
        psUpdate.setLong(5, user.getId());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete.setLong(1, (long) id);
        return psDelete.executeUpdate();
    }


    @Override
    public String getUserAvatar(long userID) throws SQLException {
        psGetUserAvatar.setLong(1, userID);
        ResultSet rs = psGetUserAvatar.executeQuery();
        String avatar = null;

        if (rs.next()) {
            avatar = rs.getString(1);
        }

        return avatar;
    }

    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
