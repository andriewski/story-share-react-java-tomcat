package dao.impl;

import dao.CommentDAO;
import db.ConnectionManager;
import dto.CommentDTO;
import entites.Comment;
import entites.Pagination;

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
public class CommentDAOImpl extends AbstractDAO implements CommentDAO {
    private static volatile CommentDAO INSTANCE = null;
    private static final String saveCommentQuery = "INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE) " +
            "VALUES (?, ?, ?, ?)";
    private static final String getCommentQuery = "SELECT * FROM COMMENTS WHERE COMMENT_ID = ?";
    private static final String updateCommentQuery = "UPDATE COMMENTS SET TEXT = ? WHERE COMMENT_ID = ?";
    private static final String deleteCommentQuery = "DELETE FROM COMMENTS WHERE COMMENT_ID = ?";

    private static final String getCommentsInThePostWithOffsetAndLimitQuery = "SELECT USERS.NAME, COMMENTS.TEXT, " +
            "COMMENTS.DATE, COMMENTS.COMMENT_ID, USERS.USER_ID FROM COMMENTS INNER JOIN USERS ON COMMENTS.USER_ID = USERS.USER_ID " +
            "WHERE POST_ID = ? ORDER BY DATE DESC LIMIT ? OFFSET ?";

    private static final String getNumberOfCommentsInThePostQuery = "SELECT COUNT(*) FROM COMMENTS WHERE POST_ID=?";

    private PreparedStatement psSave;
    private PreparedStatement psGet;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psGetCommentsInThePostWithOffsetAndLimit;
    private PreparedStatement psGetNumberOfCommentsInThePost;

    private CommentDAOImpl() {
    }

    public static CommentDAO getInstance() {
        CommentDAO localInstance = INSTANCE;

        if (localInstance == null) {
            synchronized (CommentDAOImpl.class) {
                localInstance = INSTANCE;

                if (localInstance == null) {
                    INSTANCE = localInstance = new CommentDAOImpl();
                }
            }
        }

        return localInstance;
    }

    @Override
    public Comment save(Comment comment) throws SQLException {
        psSave = prepareStatement(saveCommentQuery, Statement.RETURN_GENERATED_KEYS);
        psSave.setLong(1, comment.getUserID());
        psSave.setLong(2, comment.getPostID());
        psSave.setString(3, comment.getText());
        psSave.setTimestamp(4, comment.getDate());
        psSave.executeUpdate();
        ResultSet rs = psSave.getGeneratedKeys();

        if (rs.next()) { //Узнаем наш id в базе данных
            comment.setId(rs.getLong(1));
        }

        close(rs);

        return comment;
    }

    @Override
    public Comment get(Serializable t) throws SQLException {
        psGet = prepareStatement(getCommentQuery);
        psGet.setLong(1, (long) t);
        ResultSet rs = psGet.executeQuery();
        Comment comment = null;

        if (rs.next()) {
            comment = new Comment(rs.getLong(1), rs.getLong(2), rs.getLong(3),
                    rs.getString(4), rs.getTimestamp(5));
        }

        close(rs);

        return comment;
    }

    @Override
    public void update(Comment comment) throws SQLException {
        psUpdate = prepareStatement(updateCommentQuery);
        psUpdate.setString(1, comment.getText());
        psUpdate.setLong(2, comment.getId());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete = prepareStatement(deleteCommentQuery);
        psDelete.setLong(1, (long) id);
        return psDelete.executeUpdate();
    }

    @Override
    public List<CommentDTO> getCommentsInThePostWithOffsetAndLimit(long postID, Pagination pagination)
            throws SQLException {
        psGetCommentsInThePostWithOffsetAndLimit = prepareStatement(getCommentsInThePostWithOffsetAndLimitQuery);
        psGetCommentsInThePostWithOffsetAndLimit.setLong(1, postID);
        psGetCommentsInThePostWithOffsetAndLimit.setInt(2, pagination.getLimit());
        psGetCommentsInThePostWithOffsetAndLimit.setLong(3, pagination.getOffset());
        List<CommentDTO> listOfComments = new ArrayList<>(pagination.getLimit());
        ResultSet rs = psGetCommentsInThePostWithOffsetAndLimit.executeQuery();

        while (rs.next()) {
            listOfComments.add(new CommentDTO(rs.getString(1), rs.getString(2),
                    rs.getTimestamp(3), rs.getLong(4), rs.getLong(5)));
        }

        return listOfComments;
    }

    @Override
    public long getNumberOfCommentsInThePost(long postID) throws SQLException {
        psGetNumberOfCommentsInThePost = prepareStatement(getNumberOfCommentsInThePostQuery);
        psGetNumberOfCommentsInThePost.setLong(1, postID);
        long number = 0;
        ResultSet rs = psGetNumberOfCommentsInThePost.executeQuery();

        if (rs.next()) {
            number = rs.getLong(1);
        }

        close(rs);

        return number;
    }
}
