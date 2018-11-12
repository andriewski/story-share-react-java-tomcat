package dao.impl;

import dao.PostDAO;
import dao.UserDAO;
import db.ConnectionManager;
import dto.PostDTO;
import entites.Comment;
import entites.Like;
import entites.Pagination;
import entites.Post;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by windmill with love
 * on 15/10/2018.
 */
public class PostDAOImpl implements PostDAO {
    private static volatile PostDAO INSTANCE = null;
    private static final String savePostQuery = "INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE) VALUES (?, ?, ?, ?)";
    private static final String getPostQuery = "SELECT * FROM POSTS WHERE POST_ID = ?";
    private static final String updatePostQuery = "UPDATE POSTS SET TEXT = ?, PICTURE = ? WHERE POST_ID = ?";
    private static final String deletePostQuery = "DELETE FROM POSTS WHERE POST_ID = ?";
    private static final String getPostsWithPaginationQuery = "SELECT POST_ID, USERS.USER_ID, TEXT, DATE, USERS.NAME, " +
            "USERS.AVATAR, PICTURE, (SELECT COUNT(*) FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID) AS LIKES, " +
            "(SELECT LIKES.USER_LIKE_ID FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID AND LIKES.USER_LIKE_ID = ?) " +
            "AS THIS_POST_IS_LIKED_BY_YOU FROM POSTS INNER JOIN USERS ON POSTS.USER_ID = USERS.USER_ID " +
            "ORDER BY DATE DESC LIMIT ? OFFSET ?";
    private static final String likePostByUserQuery = "INSERT INTO LIKES (POST_ID, USER_LIKE_ID) VALUES (?, ?)";
    private static final String unlikePostByUserQuery = "DELETE FROM LIKES WHERE POST_ID = ? AND USER_LIKE_ID = ?";

    private static final String getSinglePostDTOQuery = "SELECT POST_ID, USERS.USER_ID, TEXT, DATE, USERS.NAME, " +
            "USERS.AVATAR, PICTURE, (SELECT COUNT(*) FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID) AS LIKES, " +
            "(SELECT LIKES.USER_LIKE_ID FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID AND LIKES.USER_LIKE_ID = ?) " +
            "AS THIS_POST_IS_LIKED_BY_YOU FROM POSTS INNER JOIN USERS ON POSTS.USER_ID = USERS.USER_ID WHERE POST_ID = ?";
    private static final String getNumberOfAllPosts = "SELECT COUNT(*) FROM POSTS";

    private PreparedStatement psSave;
    private PreparedStatement psGet;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psGetPostsWithPagination;
    private PreparedStatement psLikePostByUser;
    private PreparedStatement psUnlikePostByUser;
    private PreparedStatement psGetSinglePostDTO;
    private PreparedStatement psGetNumberOfAllPosts;

    {
        try {
            psSave = ConnectionManager.getConnection().prepareStatement(savePostQuery, Statement.RETURN_GENERATED_KEYS);
            psGet = ConnectionManager.getConnection().prepareStatement(getPostQuery);
            psUpdate = ConnectionManager.getConnection().prepareStatement(updatePostQuery);
            psDelete = ConnectionManager.getConnection().prepareStatement(deletePostQuery);
            psGetPostsWithPagination = ConnectionManager.getConnection().prepareStatement(getPostsWithPaginationQuery);
            psLikePostByUser = ConnectionManager.getConnection().prepareStatement(likePostByUserQuery);
            psUnlikePostByUser = ConnectionManager.getConnection().prepareStatement(unlikePostByUserQuery);
            psGetSinglePostDTO = ConnectionManager.getConnection().prepareStatement(getSinglePostDTOQuery);
            psGetNumberOfAllPosts = ConnectionManager.getConnection().prepareStatement(getNumberOfAllPosts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PostDAOImpl() {
    }

    public static PostDAO getInstance() {
        PostDAO localInstance = INSTANCE;

        if (localInstance == null) {
            synchronized (PostDAOImpl.class) {
                localInstance = INSTANCE;

                if (localInstance == null) {
                    INSTANCE = localInstance = new PostDAOImpl();
                }
            }
        }

        return localInstance;
    }

    @Override
    public Post save(Post post) throws SQLException {
        psSave.setString(1, post.getText());
        psSave.setTimestamp(2, post.getDate());
        psSave.setLong(3, post.getUserID());
        psSave.setString(4, post.getPicture());
        psSave.executeUpdate();
        ResultSet rs = psSave.getGeneratedKeys();

        if (rs.next()) { //Узнаем наш id в базе данных
            post.setId(rs.getLong(1));
        }

        close(rs);

        return post;
    }

    @Override
    public Post get(Serializable t) throws SQLException {
        psGet.setLong(1, (long) t);
        ResultSet rs = psGet.executeQuery();
        Post post = null;

        if (rs.next()) {
            post = new Post(rs.getLong(1), rs.getString(2), rs.getTimestamp(3),
                    rs.getLong(4), rs.getString(5));
        }

        close(rs);

        return post;
    }

    @Override
    public void update(Post post) throws SQLException {
        psUpdate.setString(1, post.getText());
        psUpdate.setString(2, post.getPicture());
        psUpdate.setLong(3, post.getId());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete.setLong(1, (long) id);
        return psDelete.executeUpdate();
    }

    @Override
    public List<PostDTO> getPostsWithPagination(Pagination pagination, long userID) throws SQLException {
        List<PostDTO> listOfPosts = new ArrayList<>(pagination.getLimit());
        psGetPostsWithPagination.setLong(1, userID);
        psGetPostsWithPagination.setInt(2, pagination.getLimit());
        psGetPostsWithPagination.setLong(3, pagination.getOffset());
        ResultSet rs = psGetPostsWithPagination.executeQuery();

        while (rs.next()) {
            listOfPosts.add(new PostDTO(rs.getLong(1), rs.getLong(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getString(5), rs.getString(6),
                    rs.getString(7), rs.getLong(8), rs.getLong(9) != 0));
        }

        close(rs);

        return listOfPosts;
    }

    @Override
    public int likePostByUser(long postID, long userID) throws SQLException {
        psLikePostByUser.setLong(1, postID);
        psLikePostByUser.setLong(2, userID);
        return psLikePostByUser.executeUpdate();
    }

    @Override
    public int unlikePostByUser(long postID, long userID) throws SQLException {
        psUnlikePostByUser.setLong(1, postID);
        psUnlikePostByUser.setLong(2, userID);
        return psUnlikePostByUser.executeUpdate();
    }

    @Override
    public PostDTO getSinglePostDTO(long userID, long postID) throws SQLException {
        psGetSinglePostDTO.setLong(1, userID);
        psGetSinglePostDTO.setLong(2, postID);
        ResultSet rs = psGetSinglePostDTO.executeQuery();
        PostDTO postDTO = null;

        if (rs.next()) {
            postDTO = new PostDTO(rs.getLong(1), rs.getLong(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getString(5), rs.getString(6),
                    rs.getString(7), rs.getLong(8), rs.getLong(9) != 0);
        }

        close(rs);

        return postDTO;
    }


    @Override
    public long getNumberOfAllPosts() throws SQLException {
        ResultSet rs = psGetNumberOfAllPosts.executeQuery();
        long number = 0;

        if (rs.next()) {
            number = rs.getLong(1);
        }

        close(rs);

        return number;
    }

    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
