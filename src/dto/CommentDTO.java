package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by windmill with love
 * on 19/10/2018.
 */
@Data
@AllArgsConstructor
public class CommentDTO {
    private String userName;
    private String text;
    private Date date;
}
