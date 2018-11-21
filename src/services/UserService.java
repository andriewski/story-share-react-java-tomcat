package services;

import entites.User;

public interface UserService {
    User save(User user);

    User get(String email);

    void update(User user);

    int delete(long id);

    String getUserAvatar(long userID);
}
