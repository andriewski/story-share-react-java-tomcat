package services;

import dto.MessageDTO;
import entites.Message;
import entites.Pagination;

import java.util.List;

public interface MessageService {
    Message save(Message message);

    Message get(long id);

    void update(Message message);

    int delete(long id);

    List<MessageDTO> getLastMessagesInUsersDialogs(long userID);

    List<MessageDTO> getUserMessagesWithOtherUserWithOffset(long userID, long otherUser,
                                                            Pagination pagination);

    int deleteMessageInCertainUser(long userID, long messageID);
}
