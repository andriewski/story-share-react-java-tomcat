package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by windmill with love
 * on 18/10/2018.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor //we need it for socket
public class MessageDTO {
    private String text;
    private Timestamp date;
    private long senderID;
    private long receiverID;
    private String senderName;
    private String receiverName;
}
