import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import db.ConnectionManager;
import dto.MessageDTO;
import entites.Message;
import entites.Pagination;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class TestMessageDAO {
    @Test
    public void fullTest() throws SQLException {
        MessageDAO messageDAO = MessageDAOImpl.getInstance();
        Connection cn = ConnectionManager.getConnection();
        cn.setAutoCommit(false);

        try {
            long beforeAddition = getAll("MESSAGES", cn);
            Message message = messageDAO.save(new Message("Привет, друг! Как дела?", new Timestamp(new Date().getTime()) , 1, 2));
            long afterAddition = getAll("MESSAGES", cn);
            System.out.println(beforeAddition + " " + afterAddition);
            Assert.assertNotEquals(beforeAddition, afterAddition);

            Message messageReceivedFromDB = messageDAO.get(message.getId());
            Assert.assertEquals(message, messageReceivedFromDB);

            messageReceivedFromDB.setText("Привет, ВРАГ!");
            messageDAO.update(messageReceivedFromDB);
            Message messageUpdated = messageDAO.get(message.getId());
            Assert.assertNotEquals(message, messageUpdated);

            int deletedNumber = messageDAO.delete(messageUpdated.getId());
            Assert.assertEquals(beforeAddition, afterAddition - deletedNumber);

            List<MessageDTO> listOfLastMessages = messageDAO.getLastMessagesInUsersDialogs(2);
            for (MessageDTO messageDTO : listOfLastMessages) {
                System.out.println(messageDTO.getText());
            }

            List<MessageDTO> listOfMessagesWithOtherUser = messageDAO.getUserMessagesWithOtherUserWithOffset(
                    2, 4, new Pagination(0, Integer.MAX_VALUE));
            for (MessageDTO messageDTO : listOfMessagesWithOtherUser) {
                System.out.println(messageDTO.getText());
            } ///////Надо какую-нить проверку придумать
            beforeAddition = listOfMessagesWithOtherUser.size();
            Message message4 = messageDAO.save(new Message("Але! Что за игнор?!", new Timestamp(new Date().getTime()) , 2, 4));
            listOfMessagesWithOtherUser = messageDAO.getUserMessagesWithOtherUserWithOffset(
                    2, 4, new Pagination(0, Integer.MAX_VALUE));
            afterAddition = listOfMessagesWithOtherUser.size();
            Assert.assertNotEquals(beforeAddition, afterAddition);

            deletedNumber = messageDAO.deleteMessageInCertainUser(2, message4.getId());
            Assert.assertEquals(beforeAddition, afterAddition - deletedNumber);

            listOfMessagesWithOtherUser = messageDAO.getUserMessagesWithOtherUserWithOffset(
                    2, 4, new Pagination(0, Integer.MAX_VALUE));
            afterAddition = listOfMessagesWithOtherUser.size();
            Assert.assertEquals(beforeAddition, afterAddition);

            cn.commit();
        } catch (Exception | Error e) {
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
