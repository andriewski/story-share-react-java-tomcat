package services.impl;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import entites.User;
import services.ServiceException;
import services.UserService;

import java.sql.SQLException;

public class UserServiceImpl extends AbstractService implements UserService {
    private static volatile UserService INSTANCE = null;
    UserDAO userDAO = UserDAOImpl.getInstance();

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
    public int delete(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = userDAO.delete(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting User by id " + id);
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
