import dao.UserDAO;
import dao.impl.UserDAOImpl;
import db.ConnectionManager;
import entites.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestUserDAO {
    @Test
    public void fullTest() throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection cn = ConnectionManager.getConnection();
        cn.setAutoCommit(false);

        try {
            long beforeSave = getAll("USERS", cn);
            User user = userDAO.save(new User("Test2", "Test2@tut.by", "Test2Avatar", "Test2Password"));
            long afterSave = getAll("USERS", cn);
            User userReceivedFromDB = userDAO.get(user.getEmail());
            System.out.println(beforeSave);
            System.out.println(afterSave);

            String avatar = userDAO.getUserAvatar(user.getId());
            Assert.assertEquals(avatar, user.getAvatar());

            Assert.assertNotEquals(beforeSave, afterSave);
            Assert.assertEquals(user, userReceivedFromDB);

            userDAO.delete(user.getId());
            User userAfterDelete = userDAO.get(user.getEmail());
            Assert.assertTrue(userAfterDelete.isDeleted());

            user.setAvatar("AZAZAZA");
            userDAO.update(user);
            User userAfterUpdate = userDAO.get(userReceivedFromDB.getEmail());
            Assert.assertNotEquals(user, userAfterUpdate);
            cn.prepareStatement("DELETE FROM USERS WHERE NAME LIKE 'Test%'").executeUpdate();
            long afterDeletion = getAll("USERS", cn);
            Assert.assertEquals(beforeSave, afterDeletion);

            cn.commit();
        } catch (Error | Exception e) {
            cn.rollback();
            throw e;
        }
    }

    private long getAll(String tableName, Connection cn) throws SQLException {
        String getAll = "SELECT Count(*) FROM " + tableName;
        PreparedStatement psGetAll = cn.prepareStatement(getAll);
        ResultSet rs = psGetAll.executeQuery();
        rs.next();

        return rs.getLong(1);
    }
}
