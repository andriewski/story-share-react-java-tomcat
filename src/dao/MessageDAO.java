package dao;

import dto.MessageDTO;
import entites.Message;
import entites.Pagination;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
public interface MessageDAO extends DAO<Message> {
    List<MessageDTO> getLastMessagesInUsersDialogs(long userID) throws SQLException;
    List<MessageDTO> getUserMessagesWithOtherUserWithOffset(long userID, long otherUser,
                                                            Pagination pagination) throws SQLException;
    List<MessageDTO> getAllUserMessagesWithOtherUser(long userID, long otherUser) throws SQLException;
    int deleteMessageInCertainUser(long userID, long messageID) throws SQLException;
}
