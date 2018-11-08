package entites;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
@Data
@AllArgsConstructor
public class Like {
/*CREATE TABLE LIKES (
  LIKES_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  POST_ID  INT             NOT NULL,
  CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID)
  REFERENCES POSTS (POST_ID),
  USER_LIKE_ID  INT             NOT NULL,
  CONSTRAINT FK_USER_LIKE_ID FOREIGN KEY (USER_LIKE_ID)
  REFERENCES USERS (USER_ID)
);*/
    private long id;
    private long postID;
    private long userID;
}
