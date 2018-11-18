package services.impl;

import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import dto.MessageDTO;
import entites.Message;
import entites.Pagination;
import services.MessageService;
import services.ServiceException;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl extends AbstractService implements MessageService {
    private static volatile MessageService INSTANCE = null;
    private MessageDAO messageDAO = MessageDAOImpl.getInstance();

    private MessageServiceImpl() {
    }

    @Override
    public Message save(Message message) {
        try {
            startTransaction();
            message = messageDAO.save(message);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating Message " + message);
        }

        return message;
    }

    @Override
    public Message get(long id) {
        Message message;

        try {
            startTransaction();
            message = messageDAO.get(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Message by id " + id);
        }

        return message;
    }

    @Override
    public void update(Message message) {
        try {
            startTransaction();
            messageDAO.update(message);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating Message " + message);
        }
    }

    @Override
    public int delete(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = messageDAO.delete(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting Message by id " + id);
        }

        return numberOfDeleted;
    }

    @Override
    public List<MessageDTO> getLastMessagesInUsersDialogs(long userID) {
        List<MessageDTO> list;

        try {
            startTransaction();
            list = messageDAO.getLastMessagesInUsersDialogs(userID);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Comments Last Messages In Users Dialogs by ID " + userID);
        }

        return list;
    }

    @Override
    public List<MessageDTO> getUserMessagesWithOtherUserWithOffset(long userID, long otherUser, Pagination pagination) {
        List<MessageDTO> list;

        try {
            startTransaction();
            list = messageDAO.getUserMessagesWithOtherUserWithOffset(userID, otherUser, pagination);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting User Messages With Other User With Offset by userID ")
                    .append(userID)
                    .append(" anotherUserID ")
                    .append(otherUser)
                    .append(" and pagination ")
                    .append(pagination);

            throw new ServiceException(sb.toString());
        }

        return list;
    }

    @Override
    public int deleteMessageInCertainUser(long userID, long messageID) {
        int numberOfDeletedMessage;

        try {
            startTransaction();
            numberOfDeletedMessage = messageDAO.deleteMessageInCertainUser(userID, messageID);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error deleting delete Message In Certain User by userID ")
                    .append(userID)
                    .append(" and messageID ")
                    .append(messageID);

            throw new ServiceException(sb.toString());
        }

        return numberOfDeletedMessage;
    }

    public static MessageService getInstance() {
        MessageService messageService = INSTANCE;

        if (messageService == null) {
            synchronized (MessageServiceImpl.class) {

                messageService = INSTANCE;

                if (messageService == null) {
                    INSTANCE = messageService = new MessageServiceImpl();
                }
            }
        }

        return messageService;
    }
}
