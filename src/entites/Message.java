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
public class Message {
    /*CREATE TABLE MESSAGES (
      MESSAGE_ID          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      TEXT                TEXT,
      DATE                DATETIME,
      DELETED_BY_SENDER   CHAR(1)                  DEFAULT '-',
      DELETED_BY_RECEIVER CHAR(1)                  DEFAULT '-',
      SENDER_ID           INT             NOT NULL,
      CONSTRAINT FK_SENDER_ID FOREIGN KEY (SENDER_ID)
      REFERENCES USERS (USER_ID),
      RECEIVER_ID         INT             NOT NULL,
      CONSTRAINT FK_RECEIVER_ID FOREIGN KEY (RECEIVER_ID)
      REFERENCES USERS (USER_ID)
    );*/
    private long id;
    private String text;
    private Timestamp date;
    private boolean deletedBySender;
    private boolean deletedByReceiver;
    private long senderID;
    private long receiverID;

    public Message(String text, Timestamp date, long senderID, long receiverID) {
        this.text = text;
        this.date = date;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }
}
