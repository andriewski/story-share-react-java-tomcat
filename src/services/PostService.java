package services;

import dto.PostDTO;
import entites.Pagination;
import entites.Post;

import java.util.List;

public interface PostService {
    Post save(Post post);

    Post get(long id);

    void update(Post post);

    int delete(long id);

    List<PostDTO> getPostsWithPagination(Pagination pagination, long userID);

    int likePostByUser(long postID, long userID);

    int unlikePostByUser(long postID, long userID);

    PostDTO getSinglePostDTO(long userID, long postID);

    long getNumberOfAllPosts();
}
