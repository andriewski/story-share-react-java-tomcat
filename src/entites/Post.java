package entites;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
@Data
@AllArgsConstructor
public class Post {
/*CREATE TABLE POSTS (
  POST_ID     INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  TEXT        TEXT,
  DATE        DATETIME,
  USER_ID   INT             NOT NULL,
  CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID)
  REFERENCES USERS (USER_ID),
  PICTURE     VARCHAR(255)    NOT NULL
);*/
    private long id;
    private String text;
    private Timestamp date;
    private long userID;
    private String picture;

    public Post(String text, Timestamp date, long userID, String picture) {
        this.text = text;
        this.date = date;
        this.userID = userID;
        this.picture = picture;
    }
}
