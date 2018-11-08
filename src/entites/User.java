package entites;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
@Data
@AllArgsConstructor
public class User {
    /*USER_ID     INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      NAME        VARCHAR(60)     NOT NULL,
      EMAIL       VARCHAR(255)    NOT NULL,
      AVATAR      VARCHAR(255),
      PASSWORD    VARCHAR(50)     NOT NULL,
      MESSAGES_ID INT             NOT NULL*/
    private long id;
    private String name,
            email,
            avatar,
            password;
    private boolean isDeleted;

    public User(String name, String email, String avatar, String password) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }
}
