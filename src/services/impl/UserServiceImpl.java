package services.impl;

import com.google.gson.JsonObject;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import entites.User;
import services.ServiceException;
import services.UserService;

import java.sql.SQLException;

public class UserServiceImpl extends AbstractService implements UserService {
    private static volatile UserService INSTANCE = null;
    private UserDAO userDAO = UserDAOImpl.getInstance();
    private UserServiceImpl() {
    }

    @Override
    public User save(User user) {
        try {
            startTransaction();
            user = userDAO.save(user);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating User " + user);
        }

        return user;
    }

    @Override
    public User get(String email) {
        User user;

        try {
            startTransaction();
            user = userDAO.get(email);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting User by email " + email);
        }

        return user;
    }

    @Override
    public void update(User user) {
        try {
            startTransaction();
            userDAO.update(user);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating User " + user);
        }
    }

    @Override
    public int banUser(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = userDAO.delete(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error banning User by id " + id);
        }

        return numberOfDeleted;
    }

    @Override
    public int unbanUser(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = userDAO.restoreUser(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error unbanning User by id " + id);
        }

        return numberOfDeleted;
    }

    @Override
    public String getUserAvatar(long userID) {
        String avatar;

        try {
            startTransaction();
            avatar = userDAO.getUserAvatar(userID);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting user avatar by userID " + userID);
        }

        return avatar;
    }

    @Override
    public String getUserName(long userID) {
        String name;

        try {
            startTransaction();
            name = userDAO.getUserName(userID);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting user name by userID " + userID);
        }

        return name;
    }

    @Override
    public JsonObject getUserRoleAndStatus(long userID) {
        JsonObject roleAndStatus;

        try {
            startTransaction();
            roleAndStatus = userDAO.getUserRoleAndStatus(userID);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting user role and status by userID " + userID);
        }

        return roleAndStatus;
    }

    @Override
    public int assignAdmin(long id) {
        int numberOfAdmins;

        try {
            startTransaction();
            numberOfAdmins = userDAO.assignAdmin(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error assigning like Admin by id " + id);
        }

        return numberOfAdmins;
    }

    @Override
    public int assignUser(long id) {
        int numberOfUsers;

        try {
            startTransaction();
            numberOfUsers = userDAO.assignUser(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error assigning like User by id " + id);
        }

        return numberOfUsers;
    }

    public static UserService getInstance() {
        UserService userService = INSTANCE;

        if (userService == null) {

            synchronized (UserServiceImpl.class) {
                userService = INSTANCE;

                if (userService == null) {
                    INSTANCE = userService = new UserServiceImpl();
                }
            }
        }

        return userService;
    }
}
