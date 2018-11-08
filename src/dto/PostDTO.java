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
public class PostDTO {
    private long postID;
    private long userID;
    private String text;
    private Date date;
    private String userName;
    private String userAvatar;
    private String picture;
    private long likes;
    private boolean isLiked;
}
