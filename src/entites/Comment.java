package entites;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
@Data
@AllArgsConstructor
public class Comment {
/*CREATE TABLE COMMENTS (
  USER_ID INT NOT NULL,
  CONSTRAINT FK_USER_COMMENT_ID FOREIGN KEY (USER_ID)
  REFERENCES USERS (USER_ID),
  POST_ID INT NOT NULL,
  CONSTRAINT FK_POST_COMMENT_ID FOREIGN KEY (POST_ID)
  REFERENCES POSTS (POST_ID),
  TEXT    TEXT,
  DATE    DATETIME
);*/
    private long id;
    private long userID;
    private long postID;
    private String text;
    private Timestamp date;

    public Comment(long userID, long postID, String text, Timestamp date) {
        this.userID = userID;
        this.postID = postID;
        this.text = text;
        this.date = date;
    }
}
