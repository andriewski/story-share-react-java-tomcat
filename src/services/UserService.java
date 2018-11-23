package services;

import com.google.gson.JsonObject;
import entites.User;

public interface UserService {
    User save(User user);

    User get(String email);

    void update(User user);

    String getUserAvatar(long userID);

    String getUserName(long userID);

    JsonObject getUserRoleAndStatus(long userID);

    int banUser(long id);

    int unbanUser(long id);

    int assignAdmin(long id);

    int assignUser(long id);
}
