package services.impl;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import dto.PostDTO;
import entites.Pagination;
import entites.Post;
import services.PostService;
import services.ServiceException;

import java.sql.SQLException;
import java.util.List;

public class PostServiceImpl extends AbstractService implements PostService {
    private static volatile PostService INSTANCE = null;
    private PostDAO postDAO = PostDAOImpl.getInstance();

    private PostServiceImpl() {
    }

    @Override
    public Post save(Post post) {
        try {
            startTransaction();
            post = postDAO.save(post);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating Post " + post);
        }

        return post;
    }

    @Override
    public Post get(long id) {
        Post post;

        try {
            startTransaction();
            post = postDAO.get(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Post by id " + id);
        }

        return post;
    }

    @Override
    public void update(Post post) {
        try {
            startTransaction();
            postDAO.update(post);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating Post " + post);
        }
    }

    @Override
    public int delete(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = postDAO.delete(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting Post by id " + id);
        }

        return numberOfDeleted;
    }

    @Override
    public List<PostDTO> getPostsWithPagination(Pagination pagination, long userID) {
        List<PostDTO> list;

        try {
            startTransaction();
            list = postDAO.getPostsWithPagination(pagination, userID);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting Posts With Pagination by userID ")
                    .append(userID)
                    .append(" and Pagination")
                    .append(pagination);

            throw new ServiceException(sb.toString());
        }

        return list;
    }

    @Override
    public int likePostByUser(long postID, long userID) {
        int numberOfLiked;

        try {
            startTransaction();
            numberOfLiked = postDAO.likePostByUser(postID, userID);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error liking Post by id ")
                    .append(postID)
                    .append(" and userID ")
                    .append(userID);

            throw new ServiceException(sb.toString());
        }

        return numberOfLiked;
    }

    @Override
    public int unlikePostByUser(long postID, long userID) {
        int numberOfUnliked;

        try {
            startTransaction();
            numberOfUnliked = postDAO.unlikePostByUser(postID, userID);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error unliking Post by id ")
                    .append(postID)
                    .append(" and userID ")
                    .append(userID);

            throw new ServiceException(sb.toString());
        }

        return numberOfUnliked;
    }

    @Override
    public PostDTO getSinglePostDTO(long userID, long postID) {
        PostDTO postDTO;

        try {
            startTransaction();
            postDTO = postDAO.getSinglePostDTO(userID, postID);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting Single PostDTO userID ")
                    .append(userID)
                    .append(" and postID ")
                    .append(postID);

            throw new ServiceException(sb.toString());
        }

        return postDTO;
    }

    @Override
    public long getNumberOfAllPosts() {
        long numberOfAllPosts;

        try {
            startTransaction();
            numberOfAllPosts = postDAO.getNumberOfAllPosts();
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Number Of All Posts ");
        }

        return numberOfAllPosts;
    }

    public static PostService getInstance() {
        PostService postService = INSTANCE;

        if (postService == null) {
            synchronized (PostServiceImpl.class) {

                postService = INSTANCE;

                if (postService == null) {
                    INSTANCE = postService = new PostServiceImpl();
                }
            }
        }

        return postService;
    }
}
