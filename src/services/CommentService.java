package services;

import dto.CommentDTO;
import entites.Comment;
import entites.Pagination;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);

    Comment get(long id);

    void update(Comment comment);

    int delete(long id);

    List<CommentDTO> getCommentsInThePostWithOffsetAndLimit(long postID, Pagination pagination);

    long getNumberOfCommentsInThePost(long postID);
}
