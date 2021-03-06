package dao.impl;

import com.google.gson.JsonObject;
import dao.UserDAO;
import db.ConnectionManager;
import entites.User;

import java.io.Serializable;
import java.sql.*;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */

public class UserDAOImpl extends AbstractDAO implements UserDAO {
    private static volatile UserDAO INSTANCE = null;
    private static final String saveUserQuery = "INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD) VALUES (?, ?, ?, ?)";
    private static final String getUserQuery = "SELECT * FROM USERS WHERE EMAIL=?";
    private static final String updateUserQuery = "UPDATE USERS SET NAME=?, EMAIL=?, AVATAR=?, PASSWORD=? " +
            "WHERE USER_ID=?";
    private static final String deleteUserQuery = "UPDATE USERS SET DELETED = '+' WHERE USER_ID=?";
    private static final String restoreUserQuery = "UPDATE USERS SET DELETED = '-' WHERE USER_ID=?";
    private static final String getUserAvatar = "SELECT AVATAR FROM USERS WHERE USER_ID=?";
    private static final String getUserName = "SELECT NAME FROM USERS WHERE USER_ID=?";
    private static final String getUserRoleAndStatus = "SELECT ROLE, DELETED FROM USERS WHERE USER_ID=?";
    private static final String asignAdmin = "UPDATE USERS SET ROLE = 'admin' WHERE USER_ID=?";
    private static final String asignUser = "UPDATE USERS SET ROLE = 'user' WHERE USER_ID=?";

    private PreparedStatement psSave;
    private PreparedStatement psGet;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psRestore;
    private PreparedStatement psGetUserAvatar;
    private PreparedStatement psGetUserName;
    private PreparedStatement psGetUserRoleAndStatus;
    private PreparedStatement psAssignAdmin;
    private PreparedStatement psAssignUser;

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
        psSave = prepareStatement(saveUserQuery, Statement.RETURN_GENERATED_KEYS);
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
        psGet = prepareStatement(getUserQuery);
        psGet.setString(1, t.toString());
        ResultSet rs = psGet.executeQuery();
        User user = null;

        if (rs.next()) {
            user = new User(rs.getLong(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6).equals("+"),
                    rs.getString(7));
        }

        close(rs);

        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        psUpdate = prepareStatement(updateUserQuery);
        psUpdate.setString(1, user.getName());
        psUpdate.setString(2, user.getEmail());
        psUpdate.setString(3, user.getAvatar());
        psUpdate.setString(4, user.getPassword());
        psUpdate.setLong(5, user.getId());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete = prepareStatement(deleteUserQuery);
        psDelete.setLong(1, (long) id);

        return psDelete.executeUpdate();
    }

    @Override
    public int restoreUser(long userID) throws SQLException {
        psRestore = prepareStatement(restoreUserQuery);
        psRestore.setLong(1, userID);

        return psRestore.executeUpdate();
    }

    @Override
    public String getUserAvatar(long userID) throws SQLException {
        psGetUserAvatar = prepareStatement(getUserAvatar);
        psGetUserAvatar.setLong(1, userID);
        ResultSet rs = psGetUserAvatar.executeQuery();
        String avatar = null;

        if (rs.next()) {
            avatar = rs.getString(1);
        }

        close(rs);

        return avatar;
    }

    @Override
    public String getUserName(long userID) throws SQLException {
        psGetUserName = prepareStatement(getUserName);
        psGetUserName.setLong(1, userID);
        String name = null;
        ResultSet rs = psGetUserName.executeQuery();

        if (rs.next()) {
            name = rs.getString(1);
        }

        close(rs);

        return name;
    }

    @Override
    public JsonObject getUserRoleAndStatus(long userID) throws SQLException {
        psGetUserRoleAndStatus = prepareStatement(getUserRoleAndStatus);
        psGetUserRoleAndStatus.setLong(1, userID);
        JsonObject roleAndStatus = new JsonObject();
        ResultSet rs = psGetUserRoleAndStatus.executeQuery();

        if (rs.next()) {
            roleAndStatus.addProperty("role", rs.getString(1));
            roleAndStatus.addProperty("status", rs.getString(2).equals("+") ? "banned" : "active");
        }

        close(rs);

        return roleAndStatus;
    }

    @Override
    public int assignAdmin(long userID) throws SQLException {
        psAssignAdmin = prepareStatement(asignAdmin);
        psAssignAdmin.setLong(1, userID);

        return psAssignAdmin.executeUpdate();
    }

    @Override
    public int assignUser(long userID) throws SQLException {
        psAssignUser = prepareStatement(asignUser);
        psAssignUser.setLong(1, userID);

        return psAssignUser.executeUpdate();
    }
}
