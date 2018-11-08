package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * Created by windmill with love
 * on 18/10/2018.
 */

@Data
@AllArgsConstructor
public class MessageDTO {
    private String text;
    private Date date;
    private long senderID;
    private long receiverID;
    private String senderName;
    private String receiverName;
}
