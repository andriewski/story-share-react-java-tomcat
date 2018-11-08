package dao.impl;

import dao.MessageDAO;
import db.ConnectionManager;
import dto.MessageDTO;
import entites.Message;
import entites.Pagination;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
public class MessageDAOImpl implements MessageDAO {
    private static volatile MessageDAO INSTANCE = null;
    private static final String saveMessageQuery = "INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID) " +
            "VALUES (?, ?, ?, ?)";
    private static final String getMessageQuery = "SELECT * FROM MESSAGES WHERE MESSAGE_ID=?";
    private static final String updateMessageQuery = "UPDATE MESSAGES SET TEXT=? WHERE MESSAGE_ID=?";
    private static final String deleteMessageQuery = "DELETE FROM MESSAGES WHERE MESSAGE_ID = ?";

    private static final String getUserMessagesWithOtherUserWithOffsetAndLimitQuery = "SELECT TEXT, DATE, " +
            "SENDER_ID, RECEIVER_ID, senders.NAME AS SENDER_NAME, receivers.NAME AS RECEIVER_NAME " +
            "FROM MESSAGES INNER JOIN USERS AS senders ON MESSAGES.SENDER_ID = senders.USER_ID " +
            "INNER JOIN USERS AS receivers ON MESSAGES.RECEIVER_ID = receivers.USER_ID WHERE (SENDER_ID = ? AND " +
            "RECEIVER_ID = ? AND DELETED_BY_SENDER = '-') OR (SENDER_ID = ? AND RECEIVER_ID = ? AND " +
            "DELETED_BY_RECEIVER = '-') ORDER BY DATE DESC LIMIT ? OFFSET ?";

    private static final String getLastMessagesInUsersDialogsQuery = "SELECT TEXT, DATE, " +
            "SENDER_ID, RECEIVER_ID, senders.NAME AS SENDER_NAME, receivers.NAME AS " +
            "RECEIVER_NAME FROM MESSAGES INNER JOIN USERS AS senders ON MESSAGES.SENDER_ID = senders.USER_ID " +
            "INNER JOIN USERS AS receivers ON MESSAGES.RECEIVER_ID = receivers.USER_ID INNER JOIN (SELECT MAX(MESSAGE_ID) " +
            "AS most_recent_message_id FROM MESSAGES GROUP BY CASE WHEN (SENDER_ID > RECEIVER_ID AND " +
            "((SENDER_ID = ? AND DELETED_BY_SENDER = '-') OR (RECEIVER_ID = ? AND DELETED_BY_RECEIVER = '-'))) " +
            "THEN RECEIVER_ID ELSE SENDER_ID END , CASE WHEN (SENDER_ID < RECEIVER_ID AND " +
            "((SENDER_ID = ? AND DELETED_BY_SENDER = '-') OR (RECEIVER_ID = ? AND DELETED_BY_RECEIVER = '-'))) " +
            "THEN RECEIVER_ID ELSE SENDER_ID END ) T ON T.most_recent_message_id = MESSAGES.MESSAGE_ID " +
            "WHERE (senders.USER_ID = ? AND DELETED_BY_SENDER = '-') OR " +
            "(receivers.USER_ID = ? AND DELETED_BY_RECEIVER = '-') ORDER BY MESSAGES.DATE DESC";

    private static final String deleteMessageInCertainUserQuery = "UPDATE MESSAGES SET DELETED_BY_SENDER = " +
            "CASE WHEN (SENDER_ID = ?) THEN '+' ELSE (SELECT DELETED_BY_SENDER FROM (SELECT * FROM MESSAGES) as m " +
            "WHERE MESSAGE_ID = ?) END, DELETED_BY_RECEIVER = CASE WHEN (RECEIVER_ID = ?) THEN '+' " +
            "ELSE (SELECT DELETED_BY_RECEIVER FROM (SELECT * FROM MESSAGES) as m WHERE MESSAGE_ID = ?) END " +
            "WHERE MESSAGE_ID = ?";

    private PreparedStatement psSave;
    private PreparedStatement psGet;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psGetUserMessagesWithOtherUserWithOffsetAndLimit;
    private PreparedStatement psGetLastMessagesInUsersDialogs;
    private PreparedStatement psDeleteMessageInCertainUser;

    {
        try {
            psSave = ConnectionManager.getConnection().prepareStatement(saveMessageQuery,
                    Statement.RETURN_GENERATED_KEYS);
            psGet = ConnectionManager.getConnection().prepareStatement(getMessageQuery);
            psUpdate = ConnectionManager.getConnection().prepareStatement(updateMessageQuery);
            psDelete = ConnectionManager.getConnection().prepareStatement(deleteMessageQuery);
            psGetUserMessagesWithOtherUserWithOffsetAndLimit =
                    ConnectionManager.getConnection().prepareStatement(getUserMessagesWithOtherUserWithOffsetAndLimitQuery);
            psGetLastMessagesInUsersDialogs =
                    ConnectionManager.getConnection().prepareStatement(getLastMessagesInUsersDialogsQuery);
            psDeleteMessageInCertainUser =
                    ConnectionManager.getConnection().prepareStatement(deleteMessageInCertainUserQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MessageDAOImpl() {
    }

    public static MessageDAO getInstance() {
        MessageDAO localInstance = INSTANCE;

        if (localInstance == null) {
            synchronized (MessageDAOImpl.class) {
                localInstance = INSTANCE;

                if (localInstance == null) {
                    INSTANCE = localInstance = new MessageDAOImpl();
                }
            }
        }

        return localInstance;
    }

    @Override
    public Message save(Message message) throws SQLException {
        psSave.setString(1, message.getText());
        psSave.setTimestamp(2, message.getDate());
        psSave.setLong(3, message.getSenderID());
        psSave.setLong(4, message.getReceiverID());
        psSave.executeUpdate();
        ResultSet rs = psSave.getGeneratedKeys();

        if (rs.next()) { //Узнаем наш id в базе данных
            message.setId(rs.getLong(1));
        }

        close(rs);

        return message;
    }

    @Override
    public Message get(Serializable t) throws SQLException {
        psGet.setLong(1, (long) t);
        ResultSet rs = psGet.executeQuery();
        Message message = null;

        if (rs.next()) {
            message = new Message(rs.getLong(1), rs.getString(2), rs.getTimestamp(3),
                    rs.getString(4).equals("+"), rs.getString(5).equals("+"), rs.getLong(6),
                    rs.getLong(7));
        }

        close(rs);

        return message;
    }

    @Override
    public void update(Message message) throws SQLException {
        psUpdate.setString(1, message.getText());
        psUpdate.setLong(2, message.getId());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete.setLong(1, (long) id);
        return psDelete.executeUpdate();
    }

    @Override
    public List<MessageDTO> getLastMessagesInUsersDialogs(long userID) throws SQLException {
        List<MessageDTO> listOfMessages = new ArrayList<>();
        psGetLastMessagesInUsersDialogs.setLong(1, userID);
        psGetLastMessagesInUsersDialogs.setLong(2, userID);
        psGetLastMessagesInUsersDialogs.setLong(3, userID);
        psGetLastMessagesInUsersDialogs.setLong(4, userID);
        psGetLastMessagesInUsersDialogs.setLong(5, userID);
        psGetLastMessagesInUsersDialogs.setLong(6, userID);
        ResultSet rs = psGetLastMessagesInUsersDialogs.executeQuery();

        while (rs.next()) {
            listOfMessages.add(new MessageDTO(rs.getString(1), rs.getTimestamp(2), rs.getLong(3),
                    rs.getLong(4), rs.getString(5), rs.getString(6)));
        }

        return listOfMessages;
    }

    @Override
    public List<MessageDTO> getUserMessagesWithOtherUserWithOffset(long userID, long otherUser,
                                                                   Pagination pagination) throws SQLException {
        List<MessageDTO> listOfMessages = new ArrayList<>();
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(1, userID);
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(2, otherUser);
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(3, otherUser);
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(4, userID);
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(5, pagination.getLimit());
        psGetUserMessagesWithOtherUserWithOffsetAndLimit.setLong(6, pagination.getOffset());
        ResultSet rs = psGetUserMessagesWithOtherUserWithOffsetAndLimit.executeQuery();

        while (rs.next()) {
            listOfMessages.add(new MessageDTO(rs.getString(1), rs.getTimestamp(2), rs.getLong(3),
                    rs.getLong(4), rs.getString(5), rs.getString(6)));
        }

        return listOfMessages;
    }

    @Override
    public int deleteMessageInCertainUser(long userID, long messageID) throws SQLException {
        psDeleteMessageInCertainUser.setLong(1, userID);
        psDeleteMessageInCertainUser.setLong(2, messageID);
        psDeleteMessageInCertainUser.setLong(3, userID);
        psDeleteMessageInCertainUser.setLong(4, messageID);
        psDeleteMessageInCertainUser.setLong(5, messageID);
        return psDeleteMessageInCertainUser.executeUpdate();
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
