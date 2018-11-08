package dao;

import dto.PostDTO;
import entites.Comment;
import entites.Like;
import entites.Pagination;
import entites.Post;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by windmill with love
 * on 14/10/2018.
 */
public interface PostDAO extends DAO<Post> {
    List<PostDTO> getPostsWithPagination(Pagination pagination, long userID) throws SQLException;
    int likePostByUser(long postID, long userID) throws SQLException;
    int unlikePostByUser(long postID, long userID) throws SQLException;
    PostDTO getSinglePostDTO(long userID, long postID) throws SQLException;
    long getNumberOfAllPosts() throws SQLException;
}
