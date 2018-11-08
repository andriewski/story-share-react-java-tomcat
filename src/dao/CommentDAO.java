package dao;

import dto.CommentDTO;
import entites.Comment;
import entites.Pagination;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
public interface CommentDAO extends DAO<Comment> {
    List<CommentDTO> getCommentsInThePostWithOffsetAndLimit(long postID, Pagination pagination) throws SQLException;
    long getNumberOfCommentsInThePost(long postID) throws SQLException;
}
